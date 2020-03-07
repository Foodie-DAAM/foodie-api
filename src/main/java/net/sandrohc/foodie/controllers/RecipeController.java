/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import java.util.Date;

import net.sandrohc.foodie.model.Recipe;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

//@Tag(name="Recipes", description="Gives access to available recipes.")
@RestController
@RequestMapping("/recipe")
public class RecipeController {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);

	private final RecipeService recipeService;

	private final JobLauncher jobLauncher;
	private final Job importRecipeJob;

	public RecipeController(final RecipeService recipeService,
							final JobLauncher jobLauncher,
							final Job importRecipeJob) {

		this.recipeService = recipeService;
		this.jobLauncher = jobLauncher;
		this.importRecipeJob = importRecipeJob;
	}

	@GetMapping("refresh")
	public String executeJob() {
		try {
			JobParameters params = new JobParametersBuilder()
					.addDate("started", new Date())
					.toJobParameters();

			JobExecution execution = jobLauncher.run(importRecipeJob, params);
			return execution.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

//	@ApiResponses(value = {
//			@ApiResponse(responseCode="200", description="get All Recipes")
//	})
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public Page<Recipe> getAllByPage(Pageable page) {
		PageRequest pageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSortOr(Sort.by("title")));
		return recipeService.getAllByPage(pageRequest);
	}

//	@ApiResponses(value = {
//			@ApiResponse(responseCode="200", description="get Recipe by ID"),
//			@ApiResponse(responseCode="404", description="recipe not found")
//	})
	@GetMapping(value="{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Recipe getBy(@PathVariable Integer id) {
		if (id == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID not filled");

		return recipeService.getBy(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}
