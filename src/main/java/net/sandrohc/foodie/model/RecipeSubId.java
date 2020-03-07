package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class RecipeSubId implements Serializable {

	private static final long serialVersionUID = 1L;

	private int recipeId;
	private Integer id;

	public RecipeSubId() {
	}

	public RecipeSubId(int recipeId, Integer id) {
		this.recipeId = recipeId;
		this.id = id;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public int getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeSubId that = (RecipeSubId) o;

		return recipeId == that.recipeId
			&& Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		int result = recipeId;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "RecipeSubId[" +
			   "recipeId=" + recipeId +
			   ", id=" + id +
			   ']';
	}

}
