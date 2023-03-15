package com.abel.jobListing.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.abel.jobListing.model.Post;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
class SearchRepositoryImpl implements SearchRepository {

	private static final Logger logger = LoggerFactory.getLogger(SearchRepositoryImpl.class);

	@Autowired
	private MongoClient mongoClient;

	@Autowired
	private MongoConverter mongoConverter;

	@Override
	public List<Post> search(String text) {
		List<Post> posts = new ArrayList<>();

		try (MongoDatabase database = mongoClient.getDatabase("joblisting");
				MongoCollection<Document> collection = database.getCollection("jobpost")) {

			AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
					new Document("$search", new Document("index", "default")
							.append("text", new Document("query", text)
									.append("path", Arrays.asList("techStack", "desc", "title")))),
					new Document("$sort", new Document("yoe", 1L)),
					new Document("$limit", 10L)));

			result.forEach(doc -> {
				try {
					posts.add(mongoConverter.read(Post.class, doc));
				} catch (Exception e) {
					logger.warn("Failed to convert document to Post object: {}", doc.toJson(), e);
				}
			});

		} catch (Exception e) {
			logger.error("Error searching for posts with text '{}'", text, e);
		}

		return posts;
	}
}
