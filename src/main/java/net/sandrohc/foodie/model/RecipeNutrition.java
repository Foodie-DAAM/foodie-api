/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@ToString(onlyExplicitlyIncluded=true)
public class RecipeNutrition {

	@EqualsAndHashCode.Include
	private NutritionType type;

	@EqualsAndHashCode.Include
	private float amount;

	@ToString.Include
	private String text;

	@JsonIgnore
	private String original;

}
