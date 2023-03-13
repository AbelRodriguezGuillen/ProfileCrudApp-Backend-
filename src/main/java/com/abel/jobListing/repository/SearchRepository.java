package com.abel.jobListing.repository;

import java.util.List;

import com.abel.jobListing.model.Post;

public interface SearchRepository {

	List<Post> findByText(String text);

}
