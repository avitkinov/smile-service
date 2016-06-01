package bg.vitkinov.edu.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bg.vitkinov.edu.model.Joke;

@Repository
public interface JokeRepository extends CrudRepository<Joke, Long> {

	Joke findByTitle(String title);
	
	//@Query("SELECT id, title, content FROM Joke WHERE content ILIKE CONCAT('%',:contentPart,'%')")
	//List<Joke> findUsersWithPartOfName(@Param("contentPart") String conentPart);
	
	List<Joke> findByContentIgnoreCaseContaining(String content);
}
