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
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@RequestMapping(value = "/jokeImage/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getJokeImage(@PathVariable Long id) {
		Joke joke = jokeRepository.findOne(id); 
        return joke == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(joke, HttpStatus.OK);
    }
	
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
	public ResponseEntity<?> insert(@RequestParam String title, @RequestParam String content) {
		Optional<Joke> joke = jokeRepository.findFirstByContentIgnoreCaseContaining(content);
		if (joke.isPresent()) {
			return new ResponseEntity<>(joke.get(), HttpStatus.CONFLICT);
		}
		Joke newJoke = new Joke();
		newJoke.setTitle(title);
		newJoke.setContent(content);
		newJoke.setCategory(findCategories(content));
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
}
