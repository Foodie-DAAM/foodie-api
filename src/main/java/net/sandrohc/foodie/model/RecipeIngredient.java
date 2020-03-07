package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class RecipeIngredient implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@EmbeddedId
	public RecipeSubId id = new RecipeSubId();

	@JsonBackReference
	@MapsId("recipeId")
	@ManyToOne
	public Recipe recipe;

	@Column(nullable=false)
	private String name;

	@Enumerated(EnumType.STRING)
	private UnitType type;

	@Column
	private float amount;

	@Column
	private String extra;

	@JsonIgnore
	@Column
	private String original;


	public RecipeIngredient() {
	}

	public RecipeIngredient(Recipe recipe, int id, String original, String name, UnitType type, float amount, String extra) {
		this.id.setId(id);
		this.recipe = recipe;
		this.original = original;
		this.name = name;
		this.type = type;
		this.amount = amount;
		this.extra = extra;
	}


	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public RecipeSubId getId() {
		return id;
	}

	public void setId(RecipeSubId id) {
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
			   ", value='" + original + '\'' +
			   ']';
	}
}
