/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class
})
@EnableBatchProcessing
@OpenAPIDefinition(
		info = @Info(
				title = "Foodie API",
				description = "Provides APIs for recipe data.",
				version = "1.0.0",
				license = @License(
						name = "GNU General Public License v3.0",
						url = "https://www.gnu.org/licenses/gpl-3.0.en.html"
				)
		),
		servers = {
				@Server(
						url = "http://localhost:8080",
						description = "Local API"
				),
				@Server(
						url = "https://foodie.sandrohc.net",
						description = "Live API"
				)
		}
)
public class Application {

	/*
	 * TODO
	 *  - Recipe service:
	 *    - ✓ getByIngredients
	 *    - ✓ getById
	 *    - ✓ getByTitle
	 *    - ✓ getAll (pages)
	 *  - Review service:
	 *    - ✓getReviewsByRecipeId
	 *    - getAverageReviewByRecipeId
	 *    - ✓ setReviewByRecipeId
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
