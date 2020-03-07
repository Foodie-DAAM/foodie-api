package net.sandrohc.foodie.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Unit implements Serializable {

	private static final long serialVersionUID = 1L;

	private UnitType type;
	private float amount;

	public Unit() {
	}

	public Unit(UnitType type, float amount) {
		this.type = type;
		this.amount = amount;
	}

	// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

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

	// </editor-fold>

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Unit unit = (Unit) o;

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
