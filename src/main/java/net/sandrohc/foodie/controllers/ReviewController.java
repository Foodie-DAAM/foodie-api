/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.sandrohc.foodie.model.Review;
import net.sandrohc.foodie.services.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Reviews", description = "Manages reviews for all recipes.")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}


	@GetMapping(value = "{recipeId}")
	public Flux<Review> getAllByRecipeId(@PathVariable Integer recipeId) {
		return reviewService.getAllByRecipeId(recipeId);
	}

	@GetMapping(value = "{recipeId}/avg")
	public Mono<Double> getAverageByRecipeId(@PathVariable Integer recipeId) {
		return reviewService.getAverageByRecipeId(recipeId);
	}

	@PutMapping(value = "{recipeId}")
	public void setByRecipeId(@PathVariable Integer recipeId,
									@RequestParam(value = "user") Integer userId,
									@RequestParam(value = "rating") Boolean positive) {

		reviewService.setByRecipeId(recipeId, userId, positive).subscribe();
	}

}
