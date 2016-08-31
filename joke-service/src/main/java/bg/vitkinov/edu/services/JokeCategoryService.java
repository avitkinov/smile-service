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
import bg.vitkinov.edu.model.KeyWord;
import bg.vitkinov.edu.repository.CategoryRepository;
import bg.vitkinov.edu.repository.KeyWordRepository;

/**
 * @author Asparuh Vitkinov
 */
@RestController
@RequestMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class JokeCategoryService {

	@Autowired
	private CategoryRepository repository;
	@Autowired
	private KeyWordRepository keyWordRepository;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCategory(@PathVariable Long id) {
		Category category = repository.findOne(id);
		return category == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) 
				: new ResponseEntity<>(category, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
	public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        Optional<Category> category = repository.findByName(name);
        return category.isPresent() ? new ResponseEntity<>(category.get(), HttpStatus.OK) :
        		new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }	
	
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
	public ResponseEntity<?> insert(@RequestParam String name, @RequestParam String keywords) {
		Optional<Category> category = repository.findByName(name);
		if (category.isPresent()) {
			return new ResponseEntity<>(category.get(), HttpStatus.CONFLICT);
		}
		Category newCateogry = new Category();
		newCateogry.setName(name);
		newCateogry.setKeyWords(getKewWors(keywords));
		return new ResponseEntity<>(repository.save(newCateogry), HttpStatus.CREATED);
	}

	private List<KeyWord> getKewWors(String keywords) {
		String[] words = keywords.split(",");
		List<KeyWord> result = new ArrayList<>(words.length);
		for (String word : words) {
			Optional<KeyWord> keyword = keyWordRepository.findByName(word);
			if (keyword.isPresent()) {
				result.add(keyword.get());
			} else {
				result.add(keyWordRepository.save(new KeyWord(word)));
			}
		}
		return result;
	}
}
