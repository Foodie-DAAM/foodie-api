/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

public class RecipeStep {

	private String description;
	private String url;
	private String picture;
	private Integer timer;

	public RecipeStep() {/* used by the Jackson serializer */}

	public RecipeStep(String description, String url, String picture, Integer timer) {
		this.description = description;
		this.url = url;
		this.picture = picture;
		this.timer = timer;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

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

		return description.equals(that.description);
	}

	@Override
	public int hashCode() {
		return description.hashCode();
	}

	@Override
	public String toString() {
		return "RecipeStep[" +
			   "description='" + description + '\'' +
			   ", url='" + url + '\'' +
			   ", picture='" + picture + '\'' +
			   ", timer=" + timer +
			   ']';
	}
}
