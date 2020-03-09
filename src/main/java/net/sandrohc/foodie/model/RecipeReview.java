/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

public class RecipeReview {

	private long userId;
	private boolean positive;

	public RecipeReview() {/* used by the Jackson serializer */}

	public RecipeReview(long userId, boolean positive) {
		this.userId = userId;
		this.positive = positive;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

		return userId == that.userId;
	}

	@Override
	public int hashCode() {
		return (int) (userId ^ (userId >>> 32));
	}

	@Override
	public String toString() {
		return "RecipeReview[userId=" + userId + ", positive=" + positive + ']';
	}
}
