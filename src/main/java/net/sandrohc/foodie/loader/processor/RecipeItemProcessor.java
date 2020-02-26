/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.loader.processor;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.sandrohc.foodie.loader.MissingUrlException;
import net.sandrohc.foodie.loader.model.Recipe;
import net.sandrohc.foodie.loader.model.RecipeJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class RecipeItemProcessor implements ItemProcessor<RecipeJson, Recipe> {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeItemProcessor.class);

	private static final Pattern PATTERN_ID = Pattern.compile("allrecipes\\.com/recipe/(\\d+)/");
	private static final Pattern PATTERN_SERVINGS = Pattern.compile("(\\d+) serving");


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

		processId(from, to);
		processServings(from, to);
		processIngredients(from, to);
		processSteps(from, to);
		processNutrition(from, to);

		LOG.info("Processed: " + to);

		return to;
	}

	private void processId(RecipeJson from, Recipe to) {
		Matcher matcherId = PATTERN_ID.matcher(from.getUrl());
		if (matcherId.find()) {
			long id = Long.parseLong(matcherId.group(1));
			to.setId(id);
		}
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
				.map(RecipeItemProcessor::clean)
				.map(s -> s == null ? null : s.replaceFirst("Step \\d+ ?", ""))
				.filter(Objects::nonNull)
				.collect(Collectors.joining(";")));
	}

	private void processIngredients(RecipeJson from, Recipe to) {
		to.setIngredients(from.getIngredients().stream()
				.map(RecipeItemProcessor::clean)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(";")));
	}

	private void processNutrition(RecipeJson from, Recipe to) {
		String nutrition = from.getNutrition_facts();

		if (nutrition == null) {
			return;
		}

		// remove idiosyncrasy where '.' is used as a delimiter
		nutrition = nutrition.replace(". ", ";");

		// normalize delimiters
		nutrition = nutrition.replace("\n", ";");

		to.setNutritionFacts(Arrays.stream(nutrition.split(";"))
				.map(RecipeItemProcessor::clean)
				.filter(Objects::nonNull)
				.filter(s -> !"Per Serving:".equalsIgnoreCase(s) && !"Full Nutrition".equalsIgnoreCase(s))
				.map(s -> s.replace("total fat", "fat"))
				// remove last char, if not alphanumeric
				.map(s -> Character.isLetterOrDigit(s.charAt(s.length() - 1)) ? s : s.substring(0, s.length() - 1))
				.collect(Collectors.joining(";")));
	}

	private static String clean(String str) {
		if (str == null)
			return null;

		// Remove all duplicate whitespace - https://stackoverflow.com/a/55494767/3220305
		str = str.trim().replace("  ", " ");

		if (str.startsWith("\""))
			str = str.substring(1);
		if (str.endsWith("\"") || str.endsWith(";"))
			str = str.substring(0, str.length() - 1);

		return str.isBlank() ? null : str;
	}

}