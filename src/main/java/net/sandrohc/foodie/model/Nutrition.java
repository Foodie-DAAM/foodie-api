package net.sandrohc.foodie.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Nutrition implements Serializable {

	private static final long serialVersionUID = 1L;

	private NutritionType type;
	private float amount;

	public Nutrition() {
	}

	public Nutrition(NutritionType type, float amount) {
		this.type = type;
		this.amount = amount;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

	public NutritionType getType() {
		return type;
	}

	public void setType(NutritionType type) {
		this.type = type;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Nutrition unit = (Nutrition) o;

		return type == unit.type
			&& Float.compare(unit.amount, amount) == 0;
	}

	@Override
	public int hashCode() {
		int result = type.hashCode();
		result = 31 * result + (amount != +0.0f ? Float.floatToIntBits(amount) : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Unit[" +
			   "type=" + type +
			   ", amount=" + amount +
			   ']';
	}
}
