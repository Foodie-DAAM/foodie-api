/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NutritionConverter {

	private static final double MG_TO_GRAM = 1 / 1000D;


	public static Nutrition normalize(String unit, String type, double quantity) {

		// Convert units to grams
		if (unit != null) {
			unit = unit.trim().toUpperCase();

			if (unit.startsWith("MG")) {
				quantity *= MG_TO_GRAM;
			} else if (unit.startsWith("G")) {
				quantity *= 1;
			}
		}

		// Discard anything over three decimal places
		quantity = new BigDecimal(quantity).setScale(3, RoundingMode.HALF_EVEN).doubleValue();

		if (type == null || type.isBlank()) {
			return null;
		}

		type = type.toUpperCase()
				.replace("TOTAL", "")
				.trim();

		if ("CALORIE".equals(type)) {
			type = "CALORIES";
		}

		NutritionType nutritionType;
		try {
			nutritionType = NutritionType.valueOf(type);
		} catch (IllegalArgumentException e) {
			nutritionType = null;
		}

		return nutritionType != null ? new Nutrition(nutritionType, (float) quantity) : null;
	}

}
