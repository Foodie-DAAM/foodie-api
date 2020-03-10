/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class
})
@EnableBatchProcessing
public class Application {

	/*
	 * TODO
	 *  - Recipe service:
	 *    - getByIngredients
	 *    - getById
	 *    - getByTitle
	 *    - getAll
	 *  - Review service:
	 *    - getReviewsByRecipeId
	 *    - getAverageReviewByRecipeId
	 *    - setReviewByRecipeId
	 *  - Difficulty service:
	 *    - getDifficultiesByRecipeId
	 *    - getAverageDifficultyByRecipeId
	 *    - setDifficultyByRecipeId
	 *  - Ingredients service:
	 *    - getAutocomplete
	 */

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
