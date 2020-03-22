/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.sandrohc.foodie.model.Review;
import net.sandrohc.foodie.services.ReviewService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name="Reviews", description="Gives access to review data.")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}


	@GetMapping(value="{recipeId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Flux<Review> getReviewsByRecipeId(@PathVariable Integer recipeId) {
		return reviewService.getReviewsByRecipeId(recipeId);
	}

	@PutMapping(value="{recipeId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public void setReviewByRecipeId(@PathVariable Integer recipeId,
									@RequestParam(value="user") Integer userId,
									@RequestParam(value="rating") Boolean positive) {

		reviewService.setReviewByRecipeId(recipeId, userId, positive).subscribe();
	}

}
