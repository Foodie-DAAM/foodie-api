/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RecipeNutrition {

	private NutritionType type;
	private float amount;

	@JsonIgnore
	private String original;

	public RecipeNutrition() {/* used by the Jackson serializer */}

	public RecipeNutrition(NutritionType type, float amount, String original) {
		this.type = type;
		this.amount = amount;
		this.original = original;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public NutritionType getType() {
		return type;
	}

	public void setType(NutritionType type) {
		this.type = type;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	// </editor-fold>


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeNutrition that = (RecipeNutrition) o;

		return type == that.type;
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public String toString() {
		return "RecipeNutrition[type=" + type + ", amount=" + amount + ']';
	}
}
