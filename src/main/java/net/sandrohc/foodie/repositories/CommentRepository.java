/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.repositories;

import net.sandrohc.foodie.model.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

	Flux<Comment> findAllByRecipeId(int recipeId);

	Flux<Comment> findAllByUserId(int userId);

	Flux<Comment> findAllByRecipeIdAndUserId(int recipeId, int userId);

}