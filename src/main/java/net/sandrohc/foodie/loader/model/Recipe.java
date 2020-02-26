/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.loader.model;

public class Recipe {

	private long id;
	private String url;
	private String title;
	private String description;
	private int duration;
	private int servings;
	private String picture;
	private String ingredients;
	private String steps;
	private String nutritionFacts;

	public Recipe() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public String getNutritionFacts() {
		return nutritionFacts;
	}

	public void setNutritionFacts(String nutritionFacts) {
		this.nutritionFacts = nutritionFacts;
	}

	@Override
	public String toString() {
		return "Recipe[id=" + id + ", title='" + title + "']";
	}
}
