/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import net.sandrohc.foodie.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

//@Tag(name="Reviews", description="Gives access to available recipes.")
@RestController
@RequestMapping("/review")
public class ReviewController {

	private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}


//	@ApiResponses(value = {
//			@ApiResponse(responseCode="200", description="get Recipe by ID"),
//			@ApiResponse(responseCode="404", description="recipe not found")
//	})
	@PutMapping(value="{recipeId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public void getBy(@PathVariable Integer recipeId,
					  @RequestParam(value="user") Integer userId,
					  @RequestParam(value="rating") Boolean positive) {
		if (recipeId == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID not filled");

		reviewService.set(recipeId, userId, positive);
	}

}
