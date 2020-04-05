/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.model.dto.RecipeSimple;
import net.sandrohc.foodie.services.RecipeService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name="Recipes", description="Gives access to recipe data.")
@RestController
@RequestMapping("/recipes")
public class RecipeController {

	private final JobLauncher jobLauncher;
	private final Job job;

	private final RecipeService recipeService;

	public RecipeController(
			final JobLauncher jobLauncher,
			final Job importRecipeJob,
			RecipeService recipeService) {

		this.jobLauncher = jobLauncher;
		this.job = importRecipeJob;
		this.recipeService = recipeService;
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "populate database with test data") })
	@GetMapping("digest")
	public String executeJob() {
		try {
			JobParameters params = new JobParametersBuilder()
					.addDate("started", new Date())
					.toJobParameters();

			JobExecution execution = jobLauncher.run(job, params);
			return execution.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "get all recipes") })
	@GetMapping
	public Mono<Page<RecipeSimple>> getAll(
			final @PageableDefault(sort="title") Pageable pageable) {

		return recipeService.getAll(pageable);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "get recipe by ID"),
			@ApiResponse(responseCode = "404", description = "recipe does not exist")
	})
	@GetMapping(value="{id}")
	public Mono<Recipe> getBy(@PathVariable Integer id) {
		return recipeService.getById(id);
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "search recipes with optional filters") })
	@GetMapping(value="search")
	public Mono<Page<RecipeSimple>> search(
			final @PageableDefault(sort="title") Pageable pageable,
			final @RequestParam(value="title", required=false) String title,
			final @RequestParam(value="ingredients", required=false) List<String> ingredients) {

		return recipeService.search(pageable, title, ingredients);
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "get random recipes") })
	@GetMapping(value={ "random", "random/{num}" })
	public Flux<Recipe> getRandom(@PathVariable Optional<Integer> num) {
		return recipeService.getRandom(num.orElse(1));
	}

}
