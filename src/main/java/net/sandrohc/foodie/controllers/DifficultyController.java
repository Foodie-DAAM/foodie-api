/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.sandrohc.foodie.model.Difficulty;
import net.sandrohc.foodie.model.DifficultyLevel;
import net.sandrohc.foodie.services.DifficultyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name="Difficulty", description="Manages levels for all recipes.")
@RestController
@RequestMapping("/difficulty")
public class DifficultyController {

	private final DifficultyService difficultyService;

	public DifficultyController(DifficultyService difficultyService) {
		this.difficultyService = difficultyService;
	}


	@GetMapping(value="{recipeId}")
	public Flux<Difficulty> getAllByRecipeId(@PathVariable Integer recipeId) {
		return difficultyService.getAllByRecipeId(recipeId);
	}

	@GetMapping(value="{recipeId}/avg")
	public Mono<DifficultyLevel> getAverageByRecipeId(@PathVariable Integer recipeId) {
		return difficultyService.getAverageByRecipeId(recipeId);
	}

	@PutMapping(value="{recipeId}")
	public void setByRecipeId(@PathVariable Integer recipeId,
									@RequestParam(value="user") Integer userId,
									@RequestParam(value="level") DifficultyLevel level) {

		difficultyService.setByRecipeId(recipeId, userId, level).subscribe();
	}

}
