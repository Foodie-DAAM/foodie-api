/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@SuppressWarnings("unused")
@Entity
public class Recipe {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(nullable=false)
	private String url;

	@Column(nullable=false)
	private String title;

	@Column
	private String description;

	@Column
	private Integer duration;

	@Column
	private Integer servings;

	@Column
	private String picture;

	@Column
	private String nutritionFacts;

	@OneToMany(
			mappedBy="recipe",
			cascade=CascadeType.ALL,
			orphanRemoval=true
	)
	private List<RecipeIngredient> ingredients = new ArrayList<>();

	@OneToMany(
			mappedBy="recipe",
			cascade=CascadeType.ALL,
			orphanRemoval=true
	)
	private List<RecipeStep> steps = new ArrayList<>();

	@OneToMany(
			mappedBy="recipe",
			cascade=CascadeType.ALL,
			orphanRemoval=true
	)
	private List<RecipeReview> reviews = new ArrayList<>();

	@OneToMany(
			mappedBy="recipe",
			cascade=CascadeType.ALL,
			orphanRemoval=true
	)
	private List<RecipeDifficulty> difficulties = new ArrayList<>();


	public Recipe() {
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">
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

	public String getNutritionFacts() {
		return nutritionFacts;
	}

	public void setNutritionFacts(String nutritionFacts) {
		this.nutritionFacts = nutritionFacts;
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

	public List<RecipeReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<RecipeReview> reviews) {
		this.reviews = reviews;
	}

	public List<RecipeDifficulty> getDifficulties() {
		return difficulties;
	}

	public void setDifficulties(List<RecipeDifficulty> difficulties) {
		this.difficulties = difficulties;
	}
	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Recipe recipe = (Recipe) o;

		return Objects.equals(id, recipe.id);
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "Recipe[id=" + id + ", title='" + title + "']";
	}
}
