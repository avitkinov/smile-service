/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bg.vitkinov.edu.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import bg.vitkinov.edu.model.Category;
import bg.vitkinov.edu.model.Joke;
import bg.vitkinov.edu.model.KeyWord;
import bg.vitkinov.edu.model.Service;
import bg.vitkinov.edu.repository.CategoryRepository;
import bg.vitkinov.edu.repository.JokeRepository;

/**
 * @author Asparuh Vitkinov
 */
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class JokeService {

	@Autowired
	private JokeRepository jokeRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
	
    @Bean
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }
    @RequestMapping(method = RequestMethod.GET, produces= { MediaType.APPLICATION_JSON_VALUE })
    public Service getClichedMessage() {
    	return new Service(this.getClass().getSimpleName(), "ready");
    }
    
	@RequestMapping(value = "/jokeImage/{id}", method = RequestMethod.GET, produces = {MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getJokeImage(@PathVariable Long id,
										@RequestHeader(value="Accept") String acceptType,
								 	    @RequestParam(required = false, defaultValue = "Arial-14") String font,
									    @RequestParam(required = false, defaultValue = "black") String foreColor,
									    @RequestParam(required = false) String backColor) {
		Joke joke = jokeRepository.findOne(id); 
		ServiceInstance instance = loadBalancerClient.choose("image-service");
		if (instance == null) return null;
		/*
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.set("text", Base64.getEncoder().encodeToString(joke.getContent().getBytes()));
        params.set("Accept", acceptType);
        params.set("base64", "true");
        params.set("font", font);
        params.set("foreColor", foreColor);
        params.set("foreColor", foreColor);
        params.set("backColor", backColor);
        */
		HttpHeaders params = new HttpHeaders();
		MediaType requestAcceptType = acceptType == null || "".equals(acceptType) ? MediaType.IMAGE_PNG : MediaType.parseMediaType(acceptType);
        params.setAccept(Arrays.asList(requestAcceptType));
        params.add("text", Base64.getEncoder().encodeToString(joke.getContent().getBytes()));
        params.add("base64", "true");
        params.add("font", font);
        params.add("foreColor", foreColor);
        params.add("backColor", backColor);
        
//        URI url = URI.create(String.format("%s/img", instance.getUri().toString()))
        URI url = instance.getUri().resolve("/img");
        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(null, params);
		return restTemplate.exchange(url.toString(), HttpMethod.POST, entity, byte[].class);
    }
	
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
	public ResponseEntity<?> insert(@RequestParam String title, 
			@RequestParam String content,
			@RequestParam(required = false, defaultValue = "false") boolean base64) {
		String jokeContent = base64 ? new String(Base64.getDecoder().decode(content)) : content;
		Optional<Joke> joke = jokeRepository.findFirstByContentIgnoreCaseContaining(jokeContent);
		if (joke.isPresent()) {
			return new ResponseEntity<>(joke.get(), HttpStatus.CONFLICT);
		}
		Joke newJoke = new Joke();
		newJoke.setTitle(title);
		newJoke.setContent(jokeContent);
		newJoke.setCategory(findCategories(jokeContent));
		return new ResponseEntity<>(jokeRepository.save(newJoke), HttpStatus.CREATED);
	}

	private List<Category> findCategories(String content) {
		Iterable<Category> allCategories = categoryRepository.findAll();
		List<Category> result = new ArrayList<>();
		
		for (Category category : allCategories) {
			List<KeyWord> keyWords = category.getKeyWords();
			for (KeyWord keyWord : keyWords) {
				if (content.contains(keyWord.getName())) {
					result.add(category);
					break;
				}
			}
		}
		
		return result;
	}
	
	@FeignClient("image-service")
	private interface ImageServiceClient {
		
		@RequestMapping(value = "/", method = {RequestMethod.POST}, produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE})
		byte[] convertToInlineTextImage(@RequestHeader(value="Accept") String acceptType,
											   @RequestParam String text,
											   @RequestParam(required = false, defaultValue = "false") String base64,
											   @RequestParam(required = false, defaultValue = "Arial-14") String font,
											   @RequestParam(required = false, defaultValue = "black") String foreColor,
											   @RequestParam(required = false) String backColor);
	}
}
