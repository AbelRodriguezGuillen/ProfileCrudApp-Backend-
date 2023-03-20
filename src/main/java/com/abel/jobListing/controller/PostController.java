package com.abel.jobListing.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abel.jobListing.model.Post;
import com.abel.jobListing.repository.PostRepository;
import com.abel.jobListing.repository.SearchRepository;
import com.mongodb.client.result.UpdateResult;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "{jobListing.cors.origins}")
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

	// whatever data is submitted in the client side will be accepted in post obj
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

//		return "Data deleted successfully";
		return ResponseEntity.ok().body("Post deleted successfully");

	}

	@ApiOperation(value = "Edit a post by id")
	@PutMapping("/posts/edit/{id}")
	public ResponseEntity<String> updatePostById(@PathVariable String id, @RequestBody Post post) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));

		Update update = new Update();
		update.set("title", post.getTitle());
		update.set("desc", post.getDesc());
		update.set("yoe", post.getYoe());
		update.set("techStack", post.getTechStack());

		UpdateResult result = mongoOperations.updateFirst(query, update, Post.class);

		if (result.getModifiedCount() > 0) {
			return ResponseEntity.ok().body("Post updated successfully");
		} else {
			return ResponseEntity.badRequest().body("Failed to update post");
		}
	}

}
