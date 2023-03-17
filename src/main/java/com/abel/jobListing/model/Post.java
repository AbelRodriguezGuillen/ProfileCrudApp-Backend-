package com.abel.jobListing.model;

import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "JobPost")
public class Post {

	@Id
	private String id;

	private String title;
	private String desc;
	private int yoe;
	private String techStack[];

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Post() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getYoe() {
		return yoe;
	}

	public void setYoe(int yoe) {
		this.yoe = yoe;
	}

	public String[] getTechStack() {
		return techStack;
	}

	public void setTechStack(String[] techStack) {
		this.techStack = techStack;
	}

	@Override
	public String toString() {
		return "Post [title=" + title + ", desc=" + desc + ", yoe=" + yoe + ", techStack=" + Arrays.toString(techStack)
				+ "]";
	}

}
