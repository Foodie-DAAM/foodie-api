package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class RecipeReview implements Serializable {

	@EmbeddedId
	public RecipeUserId id = new RecipeUserId();

	@MapsId("recipeId")
	@ManyToOne
	public Recipe recipe;

	@Column(nullable=false)
	private boolean positive;


	public RecipeReview() {
	}


	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeReview that = (RecipeReview) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "RecipeReview[" +
			   "id=" + id +
			   ", positive=" + positive +
			   ']';
	}
}
