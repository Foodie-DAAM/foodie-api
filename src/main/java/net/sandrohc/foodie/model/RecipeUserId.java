package net.sandrohc.foodie.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class RecipeUserId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long recipeId;
	private Long userId;


	public RecipeUserId() {
	}


	public Long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Long recipeId) {
		this.recipeId = recipeId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeUserId that = (RecipeUserId) o;

		return Objects.equals(recipeId, that.recipeId)
			&& Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		int result = recipeId != null ? recipeId.hashCode() : 0;
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "RecipeUserId[" +
			   "recipeId=" + recipeId +
			   ", userId=" + userId +
			   ']';
	}

}
