/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;

public class RecipeIngredient {

	@Indexed
	private String name;
	private UnitType type;
	private float amount;
	private String extra;

	@JsonIgnore
	private String original;

	public RecipeIngredient() {/* used by the Jackson serializer */}

	public RecipeIngredient(String name, UnitType type, float amount, String extra, String original) {
		this.name = name;
		this.type = type;
		this.amount = amount;
		this.extra = extra;
		this.original = original;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UnitType getType() {
		return type;
	}

	public void setType(UnitType type) {
		this.type = type;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
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

		RecipeIngredient that = (RecipeIngredient) o;

		if (Float.compare(that.amount, amount) != 0) return false;
		if (!name.equals(that.name)) return false;
		return type == that.type;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + (amount != +0.0f ? Float.floatToIntBits(amount) : 0);
		return result;
	}

	@Override
	public String toString() {
		return "RecipeIngredient[" +
			   "name='" + name + '\'' +
			   ", type=" + type +
			   ", amount=" + amount +
			   ", extra='" + extra + '\'' +
			   ']';
	}
}
