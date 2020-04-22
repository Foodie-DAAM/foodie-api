/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.sandrohc.foodie.services.IngredientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "Ingredients", description = "Manages ingredients list.")
@RestController
@RequestMapping("/ingredients")
public class IngredientController {

	private final IngredientService ingredientService;

	public IngredientController(IngredientService ingredientService) {
		this.ingredientService = ingredientService;
	}


	@GetMapping(value = "{name}")
	public Flux<String> findByName(@PathVariable String name) {
		return ingredientService.findByName(name);
	}

}
