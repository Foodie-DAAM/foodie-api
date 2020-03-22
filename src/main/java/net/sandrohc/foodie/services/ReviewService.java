/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.services;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.model.Review;
import net.sandrohc.foodie.repositories.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReviewService {

	private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

	private final ReviewRepository repository;

	public ReviewService(ReviewRepository repository) {
		this.repository = repository;
	}

	public Flux<Review> getAllByRecipeId(int recipeId) {
		return repository.findAllByRecipeId(recipeId);
	}

	public Mono<Double> getAverageByRecipeId(int recipeId) {
		return repository.findAverageByRecipeId(recipeId);
	}

	/**
	 * Saved the review of the specified recipe.
	 *
	 * @param recipeId The ID of the recipe.
	 * @param userId The user making the review.
	 * @param positive
	 * @return the {@link Recipe} updated.
	 */
	public Mono<Review> setByRecipeId(int recipeId, int userId, boolean positive) {
		LOG.info("New review: recipe={} user={} value={}", recipeId, userId, positive);

//		GroupOperation groupByStateAndSumPop = avg("review").as("averageReview");
//		MatchOperation filterStates = match(new Criteria("statePop").gt(10000000));
//
//		Aggregation aggregation = newAggregation(
//				groupByStateAndSumPop, filterStates, sortByPopDesc);
//		AggregationResults<StatePopulation> result = mongoTemplate.aggregate(
//				aggregation, "zips", StatePopulation.class);

		Review review = new Review(recipeId, userId, positive);
		return repository.save(review);
	}

}
