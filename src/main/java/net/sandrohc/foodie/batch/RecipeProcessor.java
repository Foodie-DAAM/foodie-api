/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.batch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.sandrohc.foodie.model.Nutrition;
import net.sandrohc.foodie.model.NutritionConverter;
import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.model.RecipeIngredient;
import net.sandrohc.foodie.model.RecipeNutrition;
import net.sandrohc.foodie.model.RecipeStep;
import net.sandrohc.foodie.model.Unit;
import net.sandrohc.foodie.model.UnitConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class RecipeProcessor implements ItemProcessor<RecipeJson, Recipe> {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeProcessor.class);

	private final Set<Integer> processed = ConcurrentHashMap.newKeySet();

	private static final Pattern PATTERN_ID         = Pattern.compile("^https?://(?:www\\.)?allrecipes\\.com/recipe/(\\d{1,10})/");
	private static final Pattern PATTERN_SERVINGS   = Pattern.compile("^(\\d{1,4})");
	private static final Pattern PATTERN_STEP       = Pattern.compile("^Step \\d{1,3} ?");
	private static final Pattern PATTERN_INGREDIENT = Pattern.compile("^\\(?(?<quantity>(?:\\d|/|\\.)+|\\\\u\\w*)? ?(?<unit>tablespoons?|teaspoons?|ounces?|fluid ounces?|cups?|pints?|quarts?|gallons?|stones?)?\\)? ?(?<name>.*?)(?:, ?(?<extra>.*))?$");
	private static final Pattern PATTERN_NUTRITION  = Pattern.compile("^ ?(?<quantity>(?:\\d|/|\\.)+|\\\\u\\w*)? ?(?<unit>g|mg?)? ?(?<type>.*)?$");


	@Override
	public Recipe process(final RecipeJson from) throws MissingUrlException, MissingIdException {
		if (from.getUrl() == null) {
			throw new MissingUrlException();
		}

		final Recipe to = new Recipe();
		to.setUrl(from.getUrl());
		to.setTitle(clean(from.getTitle()));
		to.setDescription(clean(from.getDescription()));
		to.setDuration(from.getTotal_time());
		to.setPicture(from.getPicture());

		int id = processId(from, to);

		if (processed.contains(id)) {
			LOG.trace("Recipe with ID was already processed: " + id);
			return null;
		} else {
			processed.add(id);
		}

		processServings(from, to);
		processIngredients(from, to);
		processSteps(from, to);
		processNutrition(from, to);

		LOG.trace("Processed: " + to);

		return to;
	}

	private int processId(RecipeJson from, Recipe to) throws MissingIdException {
		Matcher matcherId = PATTERN_ID.matcher(from.getUrl());

		if (!matcherId.find()) {
			throw new MissingIdException("The URL does not have an ID: " + from.getUrl());
		}

		int id = Integer.parseInt(matcherId.group(1));
		to.setId(id);
		return id;
	}

	private void processServings(RecipeJson from, Recipe to) {
		Matcher matcherServings = PATTERN_SERVINGS.matcher(from.getYields());
		if (matcherServings.find()) {
			int servings = Integer.parseInt(matcherServings.group(1));
			to.setServings(servings);
		}
	}

	private void processSteps(RecipeJson from, Recipe to) {
		AtomicInteger id = new AtomicInteger(0);

		to.setSteps(from.getInstructions().stream()
				.map(RecipeProcessor::clean)
				.map(s -> s == null ? null : PATTERN_STEP.matcher(s).replaceFirst("").trim())
				.filter(s -> s != null && !s.isEmpty())
				.map(s -> new RecipeStep(to, id.getAndIncrement(), s))
				.collect(Collectors.toList()));
	}

	private void processIngredients(RecipeJson from, Recipe to) {
		AtomicInteger id = new AtomicInteger(0);

		to.setIngredients(from.getIngredients().stream()
				.map(RecipeProcessor::clean)
				.map(s -> processIngredient(to, s, id.getAndIncrement()))
				.filter(Objects::nonNull)
				.collect(Collectors.toList()));
	}

	private RecipeIngredient processIngredient(Recipe recipe, String str, int id) {
		if (str == null) {
			return null;
		}

		Map<String, String> replacements = new HashMap<>();
		// Registered trademark
		replacements.put("®", "");
		// Fractions
		replacements.put("½", "1/2");
		replacements.put("⅓", "1/3");
		replacements.put("⅔", "2/3");
		replacements.put("¼", "1/4");
		replacements.put("¾", "3/4");
		replacements.put("⅕", "1/5");
		replacements.put("⅖", "2/5");
		replacements.put("⅗", "3/5");
		replacements.put("⅘", "4/5");
		replacements.put("⅙", "1/6");
		replacements.put("⅚", "5/6");
		replacements.put("⅛", "1/8");
		replacements.put("⅜", "3/8");
		replacements.put("⅝", "5/8");
		replacements.put("⅞", "7/8");
		replacements.put("\u2150", "1/7");
		replacements.put("\u2151", "1/9");
		replacements.put("\u2152", "1/10");

		for (Entry<String, String> replacement : replacements.entrySet()) {
			str = str.replace(replacement.getKey(), replacement.getValue());
		}

		Matcher matcher = PATTERN_INGREDIENT.matcher(str);

		if (!matcher.find()) {
			LOG.warn("Invalid ingredient: " + str);
			return null;
		}

		// name, unit, quantity
		Unit unit = new Unit();

		String quantityStr = matcher.group("quantity");
		Double quantity = processRatio(quantityStr);

		if (quantity != null) {
			String unitType = matcher.group("unit");
			unit = UnitConverter.normalize(unitType, quantity.floatValue());
			if (unit == null) {
				LOG.warn("Invalid unit type [" + unitType + "] for ingredient: " + str);
				return null;
			}
		} else {
			char ch = str.charAt(0);
			if (!Character.isLetterOrDigit(ch)) {
				LOG.warn("Invalid quantity [" + quantityStr + "] for ingredient: " + str);
			}
		}

		String name = matcher.group("name");
		String extra = matcher.group("extra");

		return new RecipeIngredient(recipe, id, str, name, unit, extra);
	}

	private Double processRatio(String str) {
		if (str == null)
			return null;

		try {
			if (str.contains("/")) {
				String[] rat = str.split("/");
				return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
			} else {
				return Double.parseDouble(str);
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private void processNutrition(RecipeJson from, Recipe to) {
		String nutrition = from.getNutrition_facts();
		if (nutrition == null)
			return;

		// remove idiosyncrasy where '.' is used as a delimiter
		nutrition = nutrition.replace(". ", ";");

		// normalize delimiters
		nutrition = nutrition.replace(";", "\n");
		nutrition = nutrition.replace("\\n", "\n");

		to.setNutritionFacts(Arrays.stream(nutrition.split("\n"))
				.map(RecipeProcessor::clean)
				.map(s -> processNutritionFact(to, s))
				.filter(Objects::nonNull)
				.collect(Collectors.toList()));
	}

	private RecipeNutrition processNutritionFact(Recipe recipe, String str) {
		if (str == null)
			return null;

		if ("Per Serving:".equalsIgnoreCase(str) || "Full Nutrition".equalsIgnoreCase(str))
			return null;

		if (str.charAt(0) == '<')
			str = str.substring(1);

		// remove last char, if not alphanumeric
		if (!Character.isLetterOrDigit(str.charAt(str.length() - 1)))
			str = str.substring(0, str.length() - 1);

		Matcher matcher = PATTERN_NUTRITION.matcher(str);

		if (!matcher.find()) {
			LOG.warn("Invalid nutrition fact: " + str);
			return null;
		}

		// quantity, unit, type
		String quantityStr = matcher.group("quantity");
		Double quantity = processRatio(quantityStr);

		if (quantity == null) {
			quantity = 0D;
			LOG.warn("Invalid quantity [" + quantityStr + "] for nutrition fact: " + str);
		}

		String unit = matcher.group("unit");
		String type = matcher.group("type");

		Nutrition nutrition = NutritionConverter.normalize(unit, type, quantity);
		if (nutrition == null) {
			LOG.warn("Invalid type [" + type + "] for nutrition fact: " + str);
			return null;
		}

		return new RecipeNutrition(recipe, str, nutrition.getType(), nutrition.getAmount());
	}

	private static String clean(String str) {
		if (str == null)
			return null;

		// Remove all duplicate whitespace - https://stackoverflow.com/a/55494767/3220305
		str = str.trim().replace("  ", " ");

		if (str.startsWith("\""))
			str = str.substring(1).trim();
		if (str.endsWith("\"") || str.endsWith(";"))
			str = str.substring(0, str.length() - 1).trim();

		return str.isEmpty() ? null : str;
	}

}