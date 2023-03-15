package com.abel.jobListing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.abel.jobListing.model.Post;
import com.abel.jobListing.repository.PostRepository;
import com.abel.jobListing.repository.SearchRepository;

public class PostControllerTests {

	@InjectMocks
	private PostController postController;

	@Mock
	private PostRepository postRepository;

	@Mock
	private SearchRepository searchRepository;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetAllPosts() {
		Post post1 = new Post("1", "Title 1", "Description 1", "Location 1", "Company 1");
		Post post2 = new Post("2", "Title 2", "Description 2", "Location 2", "Company 2");

		Mockito.when(postRepository.findAll()).thenReturn(List.of(post1, post2));

		List<Post> posts = postController.getAllPosts();

		assertEquals(2, posts.size());
		assertEquals("Title 1", posts.get(0).getTitle());
		assertEquals("Title 2", posts.get(1).getTitle());
	}

	@Test
	public void testSearch() {
		Post post1 = new Post("1", "Java Developer", "Looking for a Java developer", "San Francisco", "Acme Inc.");
		Post post2 = new Post("2", "React Developer", "Looking for a React developer", "New York", "Beta Corp.");

		Mockito.when(searchRepository.findByText("Java")).thenReturn(List.of(post1));
		Mockito.when(searchRepository.findByText("React")).thenReturn(List.of(post2));

		List<Post> javaPosts = postController.search("Java");
		assertEquals(1, javaPosts.size());
		assertEquals("Java Developer", javaPosts.get(0).getTitle());

		List<Post> reactPosts = postController.search("React");
		assertEquals(1, reactPosts.size());
		assertEquals("React Developer", reactPosts.get(0).getTitle());
	}

	@Test
	public void testAddPost() {
		Post post = new Post("1", "Title", "Description", "Location", "Company");

		Mockito.when(postRepository.save(post)).thenReturn(post);

		Post addedPost = postController.addPost(post);

		assertEquals(post, addedPost);
	}

	@Test
	public void testGetPostByIdFound() {
		Post post = new Post("1", "Title", "Description", "Location", "Company");

		Mockito.when(postRepository.findById("1")).thenReturn(java.util.Optional.of(post));

		ResponseEntity<Post> responseEntity = postController.getPostById("1");

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(post, responseEntity.getBody());
	}

	@Test
	public void testGetPostByIdNotFound() {
		Mockito.when(postRepository.findById("1")).thenReturn(java.util.Optional.empty());

		ResponseEntity<Post> responseEntity = postController.getPostById("1");

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@Test
	public void testDeletePostById() {
		Mockito.doNothing().when
		(mongoOperations).remove(Mockito.any(), Mockito.eq("JobPost"));
		ResponseEntity<String> responseEntity = postController.deletePostById("1");

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Post deleted successfully", responseEntity.getBody());
	}
