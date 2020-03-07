/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@SuppressWarnings("unused")
@Entity
public class Recipe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int id;

	@Column(nullable=false)
	private String url;

	@Column(nullable=false)
	private String title;

	@Column(length=1000)
	private String description;

	@Column
	private Integer duration;

	@Column
	private Integer servings;

	@Column
	private String picture;

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
	private List<RecipeNutrition> nutritionFacts = new ArrayList<>();

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

	public void setNutritionFacts(List<RecipeNutrition> nutrition) {
		this.nutritionFacts = nutrition;
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
