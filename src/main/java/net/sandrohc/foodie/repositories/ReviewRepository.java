/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.repositories;

import net.sandrohc.foodie.model.Review;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, Integer> {

	Flux<Review> findAllByRecipeId(int recipeId);

	@Aggregation(pipeline = {
			"{ $match: { recipeId: ?0 } }",
			"{ $group: { _id: null, average: { $avg: { $cond: [ '$positive', 1, 0 ] } } } }"
	})
	Mono<Double> findAverageByRecipeId(int recipeId);

}