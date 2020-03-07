/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.repositories;

import net.sandrohc.foodie.model.Recipe;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepositoryImplementation<Recipe, Integer>, PagingAndSortingRepository<Recipe, Integer> {

}
