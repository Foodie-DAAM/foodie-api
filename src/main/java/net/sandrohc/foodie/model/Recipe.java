/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Document
public class Recipe {

	@Id
	private int id;
	private String url;

	@Indexed
	private String title;
	private String description;
	private Integer duration;
	private Integer servings;
	private String picture;
	private List<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();
	private List<RecipeStep> steps = new ArrayList<RecipeStep>();
	private List<RecipeNutrition> nutritionFacts = new ArrayList<RecipeNutrition>();


	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "Recipe[id=" + id + ", title='" + title + "']";
	}

}
