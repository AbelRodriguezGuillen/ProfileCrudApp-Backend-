package com.abel.jobListing.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abel.jobListing.model.Post;
import com.abel.jobListing.repository.PostRepository;
import com.abel.jobListing.repository.SearchRepository;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@CrossOrigin(origins = "${joblisting.cors.origins}")
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private SearchRepository searchRepository;

	@Autowired
	private MongoOperations mongoOperations;

	@ApiIgnore
	@RequestMapping(value = "/")
	public void redirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui.html");
	}

	@ApiOperation(value = "Get all posts")
	@GetMapping("/posts")
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	@ApiOperation(value = "Search posts by text")
	@GetMapping("/posts/search/{text}")
	public List<Post> search(@PathVariable("text") String text) {
		return searchRepository.findByText(text);
	}

	@ApiOperation(value = "Add a new post")
	@PostMapping("/posts")
	public Post addPost(@RequestBody Post post) {
		return postRepository.save(post);
	}

	@ApiOperation(value = "Get a post by id")
	@GetMapping("/posts/{id}")
	public ResponseEntity<Post> getPostById(@PathVariable("id") String id) {
		Post post = postRepository.findById(id).orElse(null);
		if (post == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(post);
	}

	@ApiOperation(value = "Delete a post by id")
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<String> deletePostById(@PathVariable String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoOperations.remove(query, "JobPost");

		return ResponseEntity.ok().body("Post deleted successfully");
	}
}
