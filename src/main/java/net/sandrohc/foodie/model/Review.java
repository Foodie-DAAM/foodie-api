/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({
		@CompoundIndex(name = "recipe_user_idx", def = "{'recipeId': 1, 'userId': 1}", unique = true)
})
public class Review {

	private int recipeId;
	private long userId;
	private boolean positive;

	public Review() {/* used by the Jackson serializer */}

	public Review(int recipeId, long userId, boolean positive) {
		this.recipeId = recipeId;
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

		Review review = (Review) o;

		return recipeId != review.recipeId
			&& userId == review.userId;
	}

	@Override
	public int hashCode() {
		int result = recipeId;
		result = 31 * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "RecipeReview[recipeId=" + recipeId + ", userId=" + userId + ", positive=" + positive + ']';
	}
}
