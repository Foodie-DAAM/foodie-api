/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.services;

import net.sandrohc.foodie.model.Difficulty;
import net.sandrohc.foodie.model.DifficultyLevel;
import net.sandrohc.foodie.repositories.DifficultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DifficultyService {

	private static final Logger LOG = LoggerFactory.getLogger(DifficultyService.class);

	private final DifficultyRepository repository;

	public DifficultyService(DifficultyRepository repository) {
		this.repository = repository;
	}

	public Flux<Difficulty> getAllByRecipeId(int recipeId) {
		return repository.findAllByRecipeId(recipeId);
	}

	public Flux<Difficulty> getAllByUserId(long userId) {
		return repository.findAllByUserId(userId);
	}

	public Mono<DifficultyLevel> getAverageByRecipeId(int recipeId) {
		return repository.findAverageByRecipeId(recipeId);
	}

	/**
	 * Creates a new difficulty on the specified recipe.
	 *
	 * @param recipeId The ID of the recipe.
	 * @param userId The user making the comment.
	 * @param level The difficulty level
	 * @return the created {@link Difficulty}.
	 */
	public Mono<Difficulty> setByRecipeId(int recipeId, int userId, DifficultyLevel level) {
		Difficulty comment = new Difficulty(recipeId, userId, level);

		LOG.info("New difficulty: {}", comment);

		return repository.save(comment);
	}

	public void deleteById(int id) {
		LOG.info("Deleted difficulty: {}", id);

		repository.deleteById(id);
	}

}
