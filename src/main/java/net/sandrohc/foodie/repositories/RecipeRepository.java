/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.repositories;

import java.util.Collection;

import net.sandrohc.foodie.model.Recipe;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RecipeRepository extends ReactiveMongoRepository<Recipe, Integer> {


	@Query(value = "{ 'ingredients.name' : { $all : [?0] }}")
	Flux<Recipe> findAllByIngredients(Collection<String> ingredients);

	Flux<Recipe> findAllByTitle(String title);

	@Aggregation("{ $sample: { size: ?0 } }")
	Flux<Recipe> findRandom(int num);

}
