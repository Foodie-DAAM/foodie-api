/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.services;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.model.RecipeReview;
import net.sandrohc.foodie.repositories.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReviewService {

	private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

	private final RecipeService service;
	private final RecipeRepository repository;
	private final ReactiveMongoTemplate template;

	public ReviewService(RecipeService service, RecipeRepository repository, ReactiveMongoTemplate template) {
		this.service = service;
		this.repository = repository;
		this.template = template;
	}

	/**
	 * Looks up a recipe with a given id.
	 *
	 * @param recipeId The id to look the recipe up for.
	 * @param positive
	 * @return the {@link Recipe} updated.
	 */
	public void set(int recipeId, int userId, boolean positive) {
		LOG.info("New review: recipe={} user={} value={}", recipeId, userId, positive);

		service.getBy(recipeId).subscribe(recipe -> {
			recipe.getReviews().removeIf(r -> r.getUserId() == userId);
			recipe.getReviews().add(new RecipeReview(userId, positive));
			service.set(recipe).subscribe();
		});
	}

}
