/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.repositories;

import net.sandrohc.foodie.model.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends ReactiveMongoRepository<Recipe, Integer> {

}
