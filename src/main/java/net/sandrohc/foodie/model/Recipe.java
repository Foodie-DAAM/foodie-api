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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@ToString(onlyExplicitlyIncluded=true)
@Document
public class Recipe {

	@Id
	@ToString.Include
	@EqualsAndHashCode.Include
	private Integer id;

	@Indexed
	@ToString.Include
	private String title;
	private String url;
	private String description;
	private Integer duration;
	private Integer servings;
	private String picture;
	private List<RecipeIngredient> ingredients = new ArrayList<>();
	private List<RecipeStep> steps = new ArrayList<>();
	private List<RecipeNutrition> nutritionFacts = new ArrayList<>();

}
