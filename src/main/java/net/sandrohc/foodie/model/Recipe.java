/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recipe {

	private int id;
	private String url;
	private String title;
	private String description;
	private Integer duration;
	private Integer servings;
	private String picture;
	private List<RecipeIngredient> ingredients = new ArrayList<>();
	private List<RecipeStep> steps = new ArrayList<>();
	private List<RecipeNutrition> nutritionFacts = new ArrayList<>();
	private Set<RecipeReview> reviews = new HashSet<>();
	private Set<RecipeDifficulty> difficulties = new HashSet<>();

	public Recipe() {/* used by the Jackson serializer */}

	public Recipe(int id, String url, String title, String description, Integer duration, Integer servings,
				  String picture, List<RecipeIngredient> ingredients, List<RecipeStep> steps,
				  List<RecipeNutrition> nutritionFacts, Set<RecipeReview> reviews, Set<RecipeDifficulty> difficulties) {

		this.id = id;
		this.url = url;
		this.title = title;
		this.description = description;
		this.duration = duration;
		this.servings = servings;
		this.picture = picture;
		this.ingredients = ingredients;
		this.steps = steps;
		this.nutritionFacts = nutritionFacts;
		this.reviews = reviews;
		this.difficulties = difficulties;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getServings() {
		return servings;
	}

	public void setServings(Integer servings) {
		this.servings = servings;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public List<RecipeIngredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<RecipeIngredient> ingredients) {
		this.ingredients = ingredients;
	}

	public List<RecipeStep> getSteps() {
		return steps;
	}

	public void setSteps(List<RecipeStep> steps) {
		this.steps = steps;
	}

	public List<RecipeNutrition> getNutritionFacts() {
		return nutritionFacts;
	}

	public void setNutritionFacts(List<RecipeNutrition> nutritionFacts) {
		this.nutritionFacts = nutritionFacts;
	}

	public Set<RecipeReview> getReviews() {
		return reviews;
	}

	public void setReviews(Set<RecipeReview> reviews) {
		this.reviews = reviews;
	}

	public Set<RecipeDifficulty> getDifficulties() {
		return difficulties;
	}

	public void setDifficulties(Set<RecipeDifficulty> difficulties) {
		this.difficulties = difficulties;
	}

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Recipe recipe = (Recipe) o;

		return id == recipe.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "Recipe[id=" + id + ", title='" + title + "']";
	}

}
