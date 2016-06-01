package bg.vitkinov.edu.model;

import javax.persistence.*;

@Entity
public class Joke {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private Long id;
	private String title;
	private String content;
	private String category;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
