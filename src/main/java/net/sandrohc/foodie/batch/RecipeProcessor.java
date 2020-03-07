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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.model.RecipeIngredient;
import net.sandrohc.foodie.model.RecipeStep;
import net.sandrohc.foodie.model.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class RecipeProcessor implements ItemProcessor<RecipeJson, Recipe> {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeProcessor.class);

	private final Set<Long> processed = ConcurrentHashMap.newKeySet();

	private static final Pattern PATTERN_ID         = Pattern.compile("^https?://(?:www\\.)?allrecipes\\.com/recipe/(\\d{1,10})/");
	private static final Pattern PATTERN_SERVINGS   = Pattern.compile("^(\\d{1,4})");
	private static final Pattern PATTERN_STEP       = Pattern.compile("^Step \\d{1,3} ?");
	private static final Pattern PATTERN_INGREDIENT = Pattern.compile("^(?<quantity>(?:\\d|/)+|\\\\u\\w*)? ?(?<unit>tablespoons?|teaspoons?|cups?)? ?(?<name>.*?)(?:, ?(?<extra>.*))?$");


	@Override
	public Recipe process(final RecipeJson from) throws MissingUrlException {
		if (from.getUrl() == null) {
			throw new MissingUrlException();
		}

		final Recipe to = new Recipe();
		to.setUrl(from.getUrl());
		to.setTitle(clean(from.getTitle()));
		to.setDescription(clean(from.getDescription()));
		to.setDuration(from.getTotal_time());
		to.setPicture(from.getPicture());

		Long id = processId(from, to);

		if (id == null) {
			LOG.warn("Recipe doesn't have a valid ID: " + from.getUrl());
			return null;
		}

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

	private Long processId(RecipeJson from, Recipe to) {
		Matcher matcherId = PATTERN_ID.matcher(from.getUrl());
		if (matcherId.find()) {
			long id = Long.parseLong(matcherId.group(1));
			to.setId(id);
			return id;
		}

		return null;
	}

	private void processServings(RecipeJson from, Recipe to) {
		Matcher matcherServings = PATTERN_SERVINGS.matcher(from.getYields());
		if (matcherServings.find()) {
			int servings = Integer.parseInt(matcherServings.group(1));
			to.setServings(servings);
		}
	}

	private void processSteps(RecipeJson from, Recipe to) {
		to.setSteps(from.getInstructions().stream()
				.map(RecipeProcessor::clean)
				.map(s -> s == null ? null : PATTERN_STEP.matcher(s).replaceFirst("").trim())
				.filter(s -> s != null && !s.isEmpty())
				.map(s -> new RecipeStep(to, s))
				.collect(Collectors.toList()));
	}

	private void processIngredients(RecipeJson from, Recipe to) {
		to.setIngredients(from.getIngredients().stream()
				.map(RecipeProcessor::clean)
				.filter(Objects::nonNull)
				.map(s -> processIngredient(to, s))
				.filter(Objects::nonNull)
				.collect(Collectors.toList()));
	}

	private RecipeIngredient processIngredient(Recipe recipe, String str) {
		// name, unit, quantity

		Map<String, String> replacements = new HashMap<>();
		replacements.put("\u00bc", "1/4");
		replacements.put("\u00bd", "1/2");
		replacements.put("\u00be", "3/4");
		replacements.put("\u2150", "1/7");
		replacements.put("\u2151", "1/9");
		replacements.put("\u2152", "1/10");
		replacements.put("\u2153", "1/3");
		replacements.put("\u2154", "2/3");
		replacements.put("\u2155", "1/5");
		replacements.put("\u2156", "2/5");
		replacements.put("\u2157", "3/5");
		replacements.put("\u2158", "4/5");

		for (Entry<String, String> replacement : replacements.entrySet()) {
			str = str.replace(replacement.getKey(), replacement.getValue());
		}

		Matcher matcher = PATTERN_INGREDIENT.matcher(str);

		if (!matcher.find()) {
			LOG.warn("INVALID INGREDIENT: " + str);
			return null;
		}

		// TODO
		String quantity = matcher.group("quantity");
		String unit = matcher.group("unit");
		String name = matcher.group("name");
		String extra = matcher.group("extra");

		return new RecipeIngredient(recipe, name, Unit.GRAMS, 0, extra);
	}

	private void processNutrition(RecipeJson from, Recipe to) {
		String nutrition = from.getNutrition_facts();

		if (nutrition == null) {
			return;
		}

		// remove idiosyncrasy where '.' is used as a delimiter
		nutrition = nutrition.replace(". ", ";");

		// normalize delimiters
		nutrition = nutrition.replace(";", "\n");
		nutrition = nutrition.replace("\\n", "\n");

		to.setNutritionFacts(Arrays.stream(nutrition.split("\n"))
				.map(RecipeProcessor::clean)
				.filter(Objects::nonNull)
				.filter(s -> !"Per Serving:".equalsIgnoreCase(s) && !"Full Nutrition".equalsIgnoreCase(s))
				.map(s -> s.replace("total fat", "fat"))
				// remove last char, if not alphanumeric
				.map(s -> Character.isLetterOrDigit(s.charAt(s.length() - 1)) ? s : s.substring(0, s.length() - 1))
				.collect(Collectors.joining("\n")));
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