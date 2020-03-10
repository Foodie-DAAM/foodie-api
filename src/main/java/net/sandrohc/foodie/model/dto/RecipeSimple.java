package net.sandrohc.foodie.model.dto;

import net.sandrohc.foodie.model.Recipe;
import org.springframework.data.annotation.Id;

public class RecipeSimple {

	@Id
	private int id;
	private String title;
	private String description;
	private int duration;
	private int servings;

	public RecipeSimple() {
	}

	public RecipeSimple(Recipe recipe) {
		this.id = recipe.getId();
		this.title = recipe.getTitle();
		this.description = recipe.getDescription();
		this.duration = recipe.getDuration();
		this.servings = recipe.getServings();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeSimple that = (RecipeSimple) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "RecipeSimple[id=" + id + ", title='" + title + "']";
	}
}
