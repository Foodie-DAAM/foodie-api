/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.repositories;

import net.sandrohc.foodie.model.Difficulty;
import net.sandrohc.foodie.model.DifficultyLevel;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DifficultyRepository extends ReactiveMongoRepository<Difficulty, Integer> {

	Flux<Difficulty> findAllByRecipeId(int recipeId);

	Flux<Difficulty> findAllByUserId(long userId);

	Flux<Difficulty> findAllByRecipeIdAndUserId(int recipeId, int userId);

	@Aggregation(pipeline = {
			"{ $match: { recipeId: ?0 } }",
			"{ $sortByCount: '$level' }",
			"{ $limit: 1 }",
			"{ $project: { _id: 0, level: '$_id' } }"
	})
	Mono<DifficultyLevel> findAverageByRecipeId(int recipeId);

}