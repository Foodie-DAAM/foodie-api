/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import java.util.Date;
import java.util.List;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.model.dto.RecipeSimple;
import net.sandrohc.foodie.services.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Tag(name="Recipes", description="Gives access to available recipes.")
@RestController
@RequestMapping("/recipes")
public class RecipeController {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);

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



	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Page<RecipeSimple>> getAll(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="sort", defaultValue="title") String sort) {

		return recipeService.getAll(PageRequest.of(page, size, Sort.by(sort)));
	}



	@GetMapping(value="{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Recipe> getBy(@PathVariable Integer id) {
		return recipeService.getBy(id);
	}

	@GetMapping(value="ingredient/{ingredient}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Flux<Recipe> getBy(@PathVariable String ingredient) {
		return recipeService.getByIngredient(ingredient);
	}

	@GetMapping(value="ingredients/{ingredient}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Flux<Recipe> getByIngredients(@PathVariable String[] ingredient) {
		return recipeService.getByIngredients(ingredient);
	}

	@GetMapping(value="random", produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Recipe> getRandom() {
		return recipeService.getRandom();
	}

}
