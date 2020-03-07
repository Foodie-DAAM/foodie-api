/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

public class UnitConverter {

	/* U.S. measures */
	/** volume **/
	// smaller
	private static final float OUNCE_TO_CUP      = 1 / 8F;                // 1/8
	private static final float TABLESPOON_TO_CUP = OUNCE_TO_CUP / 2;      // 1/16
	private static final float TEASPOON_TO_CUP   = TABLESPOON_TO_CUP / 3; // 1/48
	// larger
	private static final float PINT_TO_CUP       = 2;                     // 2
	private static final float QUART_TO_CUP      = PINT_TO_CUP  * 2;      // 4
	private static final float GALLON_TO_CUP     = QUART_TO_CUP * 4;      // 16

	/** weight **/
	private static final float OUNCE_TO_POUND    = 1 / 16F;
	private static final float STONE_TO_POUND    = 1 / 14F;
	private static final float TON_TO_POUND      = 2000;

	/* Metric measures */
	private static final float CUP_TO_MILLILITER = 240;


	public static Unit normalize(String type, float quantity) {
		if (type == null || type.isBlank()) {
			return new Unit(UnitType.TYPELESS, quantity);
		}

		type = type.trim().toUpperCase();

		// Volumes
		if (type.startsWith("TEASPOON")) {
			return new Unit(UnitType.VOLUME, quantity * TEASPOON_TO_CUP);
		} else if (type.startsWith("TABLESPOON")) {
			return new Unit(UnitType.VOLUME, quantity * TABLESPOON_TO_CUP);
		} else if (type.startsWith("FLUID OUNCE")) {
			return new Unit(UnitType.VOLUME, quantity * OUNCE_TO_CUP);
		} else if (type.startsWith("CUP")) {
			return new Unit(UnitType.VOLUME, quantity);
		} else if (type.startsWith("PINT")) {
			return new Unit(UnitType.VOLUME, quantity * PINT_TO_CUP);
		} else if (type.startsWith("QUART")) {
			return new Unit(UnitType.VOLUME, quantity * QUART_TO_CUP);
		} else if (type.startsWith("GALLON")) {
			return new Unit(UnitType.VOLUME, quantity * GALLON_TO_CUP);
		}

		// Weights
		if (type.startsWith("OUNCE")) {
			return new Unit(UnitType.WEIGHT, quantity * OUNCE_TO_POUND);
		} else if (type.startsWith("STONE")) {
			return new Unit(UnitType.WEIGHT, quantity * STONE_TO_POUND);
		} else if (type.startsWith("POUND")) {
			return new Unit(UnitType.WEIGHT, quantity);
		} else if (type.startsWith("TON")) {
			return new Unit(UnitType.WEIGHT, quantity * TON_TO_POUND);
		}

		return null;
	}

}
