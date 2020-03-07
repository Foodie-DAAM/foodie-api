/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import java.util.Collections;
import java.util.stream.Stream;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.services.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

//@Tag(name="Recipes", description="Gives access to available recipes.")
@RestController
@RequestMapping("/recipe")
public class RecipeController {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);

	private final RecipeService recipeService;

	private final JobLauncher jobLauncher;
	private final JobExplorer jobExplorer;
	private final JobRepository jobRepository;
	private final Job importRecipeJob;

	public RecipeController(final RecipeService recipeService,
							final JobLauncher jobLauncher,
							final JobExplorer jobExplorer,
							final JobRepository jobRepository,
							final Job importRecipeJob) {

		this.recipeService = recipeService;
		this.jobLauncher = jobLauncher;
		this.jobExplorer = jobExplorer;
		this.jobRepository = jobRepository;
		this.importRecipeJob = importRecipeJob;
	}

	@GetMapping("refresh")
	public String executeJob() {
		try {
			JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
			runner.setJobs(Collections.singleton(importRecipeJob));
//			runner.setJobNames("load recipes");
			runner.setApplicationEventPublisher(event -> LOG.debug("" + ((JobExecutionEvent) event).getJobExecution()));
			runner.run();
			return "done";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

//	@ApiResponses(value = {
//			@ApiResponse(responseCode="200", description="get All Recipes")
//	})
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public Stream<Recipe> getAllByPage(
			@RequestParam("page") int pageIndex,
			@RequestParam("size") int pageSize
	) {
		return recipeService.getAllByPage(PageRequest.of(pageIndex, pageSize, Sort.by("title")));
	}

//	@ApiResponses(value = {
//			@ApiResponse(responseCode="200", description="get Recipe by ID"),
//			@ApiResponse(responseCode="404", description="recipe not found")
//	})
	@GetMapping(value="{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Recipe getBy(@PathVariable Long id) {
		if (id == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID not filled");

		return recipeService.getBy(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}
