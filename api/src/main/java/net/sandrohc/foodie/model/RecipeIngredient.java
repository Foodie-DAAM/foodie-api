package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class RecipeIngredient implements Serializable {

	@EmbeddedId
	public RecipeStepId id = new RecipeStepId(); // TODO: why RecipeStepId?

	@MapsId("recipeId")
	@ManyToOne
	public Recipe recipe;

	@Column(nullable=false)
	private String name;

	@Column
	private Unit unit;

	@Column
	private Integer quantity;

	@Column
	private String extra;


	public RecipeIngredient() {
	}

	public RecipeIngredient(Recipe recipe, String name, Unit unit, int quantity, String extra) {
		this.recipe = recipe;
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
		this.extra = extra;
	}


	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public RecipeStepId getId() {
		return id;
	}

	public void setId(RecipeStepId id) {
		this.id = id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer amount) {
		this.quantity = amount;
	}

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeIngredient that = (RecipeIngredient) o;

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
			   ", name='" + name + '\'' +
			   ", unit=" + unit +
			   ", amount=" + quantity +
			   ", extra='" + extra + '\'' +
			   ']';
	}
}
