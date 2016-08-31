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
package bg.vitkinov.edu.model;

import java.util.List;
import javax.persistence.*;

/**
 * @author Asparuh Vitkinov
 */
@Entity
public class Joke {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private Long id;
	@Column(unique = true, nullable = false)
	private String title;
	@Column(unique = true, nullable = false, columnDefinition="TEXT")
	private String content;
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Category> categories;
	
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
	public List<Category> getCategory() {
		return categories;
	}
	public void setCategory(List<Category> categories) {
		this.categories = categories;
	}
}
