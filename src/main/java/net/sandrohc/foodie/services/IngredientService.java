/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.services;

import java.util.Comparator;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.model.RecipeIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Component
public class IngredientService {

	private static final Logger LOG = LoggerFactory.getLogger(IngredientService.class);

	private final ReactiveMongoTemplate template;

	public IngredientService(ReactiveMongoTemplate template) {
		this.template = template;
	}

	/**
	 * Looks up a list of up to 10 similar ingredients.
	 *
	 * @param name The name of the ingredient.
	 * @return a list of similar ingredients.
	 */
	public Flux<String> findByName(String name) {
		String nameNormalized = name.trim().toLowerCase();

		LOG.info("Finding ingredients for: " + nameNormalized);

		Query query = new Query();
		query.addCriteria(Criteria.where("ingredients.name").regex(nameNormalized + ".*", "i"));
		query.fields().include("ingredients.name");

//		return template.findDistinct(query, "ingredients.name", Recipe.class, String.class)
//				.limitRequest(1000)
//				.filter(str -> str.toLowerCase().startsWith(nameNormalized))
//				.map(str -> str + '\n')
//				.sort();

		return template.find(query, Recipe.class)
				.limitRequest(250)
				.flatMap(r -> Flux.fromIterable(r.getIngredients()))
				.map(RecipeIngredient::getName)
				.filter(str -> str.toLowerCase().startsWith(nameNormalized))
				.map(str -> str + '\n')
				.distinct(Object::hashCode)
				.sort(Comparator.comparing(String::toLowerCase));
	}

}
