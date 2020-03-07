package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrePersist;

@Entity
public class RecipeNutrition implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public RecipeNutritionId id = new RecipeNutritionId();

	@MapsId("recipeId")
	@ManyToOne
	public Recipe recipe;

	@Column(nullable=false)
	private float amount;

	@Column
	private String original;


	public RecipeNutrition() {
	}

	public RecipeNutrition(Recipe recipe, String original, NutritionType type, float amount) {
		this.id.setType(type.name());
		this.recipe = recipe;
		this.original = original;
		this.amount = amount;
	}

	@PrePersist
	private void prePersist(){
		if (getId() == null){
			RecipeNutritionId id = new RecipeNutritionId();
			id.setRecipeId(getRecipe().getId());
			id.setType(getType().toString());
			this.setId(id);
		}
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public RecipeNutritionId getId() {
		return id;
	}

	public void setId(RecipeNutritionId id) {
		this.id = id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public NutritionType getType() {
		return NutritionType.valueOf(id.getType());
	}

	public void setType(NutritionType type) {
		this.id.setType(type.name());
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

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "RecipeIngredient[" +
			   "id=" + id +
			   ", recipe=" + recipe +
			   ", type=" + getType() +
			   ", amount=" + amount +
			   ']';
	}
}
