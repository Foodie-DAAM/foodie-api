/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

public class Difficulty {

	private int recipeId;
	private long userId;
	private DifficultyLevel difficultyLevel;

	public Difficulty() {/* used by the Jackson serializer */}

	public Difficulty(int recipeId, long userId, DifficultyLevel difficultyLevel) {
		this.recipeId = recipeId;
		this.userId = userId;
		this.difficultyLevel = difficultyLevel;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	// </editor-fold>


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Difficulty that = (Difficulty) o;

		return userId == that.userId
			&& difficultyLevel == that.difficultyLevel;
	}

	@Override
	public int hashCode() {
		int result = (int) (userId ^ (userId >>> 32));
		result = 31 * result + (difficultyLevel != null ? difficultyLevel.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "RecipeDifficulty[recipeId=" + recipeId + ", userId=" + userId + ", difficulty=" + difficultyLevel + ']';
	}
}
