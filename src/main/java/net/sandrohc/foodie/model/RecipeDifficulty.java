package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class RecipeDifficulty implements Serializable {

	@EmbeddedId
	public RecipeUserId id = new RecipeUserId();

	@MapsId("recipeId")
	@ManyToOne
	public Recipe recipe;

	@Column(nullable=false)
	private Difficulty difficulty;


	public RecipeDifficulty() {
	}


	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public RecipeUserId getId() {
		return id;
	}

	public void setId(RecipeUserId id) {
		this.id = id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeDifficulty that = (RecipeDifficulty) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "RecipeDifficulty[" +
			   "id=" + id +
			   ", difficulty=" + difficulty +
			   ']';
	}

}
