/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.api;

import net.sandrohc.foodie.controllers.RecipeController;
import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.repositories.RecipeRepository;
import net.sandrohc.foodie.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers=RecipeController.class)
@Import(RecipeService.class)
public class RecipeControllerTest {

	@MockBean RecipeRepository repository;
	@MockBean Job job;
	@MockBean ReactiveMongoTemplate reactiveMongoTemplate;

	@Autowired
	private WebTestClient webClient;

	@Test
	void testGetRecipeById() {
		Recipe recipe = new Recipe();
		recipe.setId(1);
		recipe.setTitle("Test");

		Mono<Recipe> recipeFlux = Mono.just(recipe);

		Mockito
				.when(repository.findById(1))
				.thenReturn(recipeFlux);

		webClient.get().uri("/recipe/{id}", 1)
				.header(HttpHeaders.ACCEPT, "application/json")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(1)
				.jsonPath("$.title").isNotEmpty()
				.jsonPath("$.title").isEqualTo("Test");

		Mockito.verify(repository, times(1)).findById(1);
	}

}
