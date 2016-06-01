package bg.vitkinov.edu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bg.vitkinov.edu.model.Joke;
import bg.vitkinov.edu.repository.JokeRepository;

@RestController
@RequestMapping(value = "/jokes", produces = MediaType.APPLICATION_JSON_VALUE)
public class JokeService {

	@Autowired
	JokeRepository jokeRepository;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Joke convertToInlineTextImage(@PathVariable Long id) {
        return jokeRepository.findOne(id);
    }
}
