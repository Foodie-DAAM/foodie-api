/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.sandrohc.foodie.model.Comment;
import net.sandrohc.foodie.services.CommentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "Comments", description = "Manages comments for all recipes.")
@RestController
@RequestMapping("/comments")
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}


	@GetMapping(value = "recipe/{recipeId}")
	public Flux<Comment> getAllByRecipeId(@PathVariable Integer recipeId) {
		return commentService.getAllCommentsByRecipeId(recipeId);
	}

	@PutMapping(value = "recipe/{recipeId}")
	public void setByRecipeId(@PathVariable Integer recipeId,
							  @RequestParam(value = "user") Integer userId,
							  @RequestParam(value = "text") String text) {

		commentService.setByRecipeId(recipeId, userId, text).subscribe();
	}

	@GetMapping(value = "user/{userId}")
	public Flux<Comment> getAllByUserId(@PathVariable Integer userId) {
		return commentService.getAllCommentsByUserId(userId);
	}

	@DeleteMapping(value = "{id}")
	public void deleteById(@PathVariable String id) {
		commentService.deleteById(id);
	}

}
