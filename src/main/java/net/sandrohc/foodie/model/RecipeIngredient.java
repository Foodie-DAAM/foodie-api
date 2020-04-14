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
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@ToString(onlyExplicitlyIncluded=true)
public class RecipeIngredient {

	@Indexed
	@EqualsAndHashCode.Include
	@ToString.Include
	private String name;

	@EqualsAndHashCode.Include
	private UnitType type;

	@EqualsAndHashCode.Include
	private float amount;
	private String extra;

	@JsonIgnore
	private String original;

	@ToString.Include
	private String textImperial;

	@ToString.Include
	private String textMetric;

}
