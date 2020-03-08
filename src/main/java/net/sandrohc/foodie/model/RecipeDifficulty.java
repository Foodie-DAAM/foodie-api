/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

public class RecipeDifficulty {

	private long userId;
	private Difficulty difficulty;

	public RecipeDifficulty() {/* used by the Jackson serializer */}

	public RecipeDifficulty(long userId, Difficulty difficulty) {
		this.userId = userId;
		this.difficulty = difficulty;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

		return userId == that.userId
			&& difficulty == that.difficulty;
	}

	@Override
	public int hashCode() {
		int result = (int) (userId ^ (userId >>> 32));
		result = 31 * result + (difficulty != null ? difficulty.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "RecipeDifficulty[userId=" + userId + ", difficulty=" + difficulty + ']';
	}
}
