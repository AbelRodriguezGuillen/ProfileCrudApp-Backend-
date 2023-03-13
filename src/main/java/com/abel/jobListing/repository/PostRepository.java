package com.abel.jobListing.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.abel.jobListing.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

}
