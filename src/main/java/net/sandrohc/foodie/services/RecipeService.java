/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.services;

import java.util.Optional;
import java.util.stream.Stream;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.repositories.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class RecipeService {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeService.class);
	private final RecipeRepository repository;

	public RecipeService(RecipeRepository repository) {
		this.repository = repository;
	}

	/**
	 * Looks up a recipe with a given id.
	 *
	 * @param id The id to look the recipe up for.
	 * @return the requested {@link Recipe}.
	 */
	public Optional<Recipe> getBy(Integer id) {
		LOG.info("Finding entity: {}", id);
		return repository.findById(id);
	}

	public Stream<Recipe> getAllByPage(PageRequest of) {
		LOG.info("Find for: " + of);
		return repository.findAll(of).get();
	}

}
