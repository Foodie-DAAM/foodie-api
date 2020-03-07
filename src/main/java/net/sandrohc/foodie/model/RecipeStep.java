package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class RecipeStep implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public RecipeSubId id = new RecipeSubId();

	@MapsId("recipeId")
	@ManyToOne
	public Recipe recipe;

	@Column(nullable=false, length=1000)
	private String description;

	@Column
	private String url;

	@Column
	private String picture;

	@Column
	private Integer timer;


	public RecipeStep() {
	}

	public RecipeStep(Recipe recipe, int id, String description) {
		this.getId().setId(id);
		this.recipe = recipe;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getTimer() {
		return timer;
	}

	public void setTimer(Integer timer) {
		this.timer = timer;
	}

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeStep that = (RecipeStep) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "RecipeStep[" +
			   "id=" + id +
			   ", description='" + description + '\'' +
			   ", url='" + url + '\'' +
			   ", picture='" + picture + '\'' +
			   ", timer=" + timer +
			   ']';
	}
}
