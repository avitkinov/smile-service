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

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	
	@RequestMapping(value = "/jokeImage/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getJokeImage(@PathVariable Long id,
										@RequestHeader(value="Accept") String acceptType,
										@RequestParam(required = false, defaultValue = "false") String base64,
								 	    @RequestParam(required = false, defaultValue = "Arial-14") String font,
									    @RequestParam(required = false, defaultValue = "black") String foreColor,
									    @RequestParam(required = false) String backColor) {
	//	Joke joke = jokeRepository.findOne(id); 
		return restTemplate.exchange("http://image-service/", HttpMethod.POST, null, byte[].class);
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
