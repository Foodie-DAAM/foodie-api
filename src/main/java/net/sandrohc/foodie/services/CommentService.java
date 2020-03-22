/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.services;

import net.sandrohc.foodie.model.Comment;
import net.sandrohc.foodie.repositories.CommentRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CommentService {

	private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

	private final CommentRepository repository;

	public CommentService(CommentRepository repository) {
		this.repository = repository;
	}

	public Flux<Comment> getAllCommentsByRecipeId(int recipeId) {
		return repository.findAllByRecipeId(recipeId);
	}

	public Flux<Comment> getAllCommentsByUserId(int userId) {
		return repository.findAllByUserId(userId);
	}

	/**
	 * Creates a new comment on the specified recipe.
	 *
	 * @param recipeId The ID of the recipe.
	 * @param userId The user making the comment.
	 * @param text The comment text
	 * @return the created {@link Comment}.
	 */
	public Mono<Comment> setByRecipeId(int recipeId, int userId, String text) {
		Comment comment = new Comment(null, recipeId, userId, text, null);

		LOG.info("New comment: {}", comment);

		return repository.save(comment);
	}

	public void deleteById(String id) {
		LOG.info("Deleted comment: {}", id);

		repository.deleteById(id);
	}

}
