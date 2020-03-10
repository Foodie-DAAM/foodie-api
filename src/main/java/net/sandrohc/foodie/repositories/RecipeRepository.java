/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.repositories;

import java.util.List;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.model.RecipeIngredient;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RecipeRepository extends ReactiveMongoRepository<Recipe, Integer> {

	@Aggregation("{ $sample: { size: 1 } }")
	Mono<Recipe> findRandom();

	Flux<Recipe> findAllByIngredients(List<RecipeIngredient> recipeIngredient);

	Flux<Recipe> findByTitle(String title);

}
