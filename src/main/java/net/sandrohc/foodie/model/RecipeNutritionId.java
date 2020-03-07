package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RecipeNutritionId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="recipeId", nullable=false, insertable=false, updatable=false)
	private int recipeId;

	@Column(name="type", nullable=false, insertable=false, updatable=false)
	private String type;

	public RecipeNutritionId() {
	}

	public RecipeNutritionId(int recipeId, String type) {
		this.recipeId = recipeId;
		this.type = type;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public int getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeNutritionId that = (RecipeNutritionId) o;

		return recipeId == that.recipeId
			&& Objects.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		int result = recipeId;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "RecipeSubId[" +
			   "recipeId=" + recipeId +
			   ", type=" + type +
			   ']';
	}

}
