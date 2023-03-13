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

import springfox.documentation.annotations.ApiIgnore;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

	@Autowired
	PostRepository repo;

	@Autowired
	SearchRepository sRepo;

	@Autowired
	private MongoOperations mongoOperations;

	@ApiIgnore
	@RequestMapping(value = "/")
	public void redirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui.html");
	}

	@GetMapping("/allPosts")
	@CrossOrigin
	public List<Post> getAllPosts() {
		return repo.findAll();
	}

	@GetMapping("/posts/{text}")
	@CrossOrigin
	public List<Post> search(@PathVariable("text") String text) {
		return sRepo.findByText(text);
	}

	@PostMapping("/post") // whatever data is submitted in the client side will be accepted in post obj
	public Post addPost(@RequestBody Post post) {
		return repo.save(post);
	}

	// Get 1 Post
	@GetMapping("/post/{id}")
	@CrossOrigin
	public ResponseEntity<Post> getPostById(@PathVariable("id") String id) {
		Post post = repo.findById(id).orElse(null);
		if (post == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(post);
	}

	// Delete Post
	@DeleteMapping("/post/{id}")
	@CrossOrigin
	public String deleteDataById(@PathVariable String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoOperations.remove(query, "JobPost");

		return "Data deleted successfully";
	}
}
