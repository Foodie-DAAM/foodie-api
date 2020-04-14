/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.batch;

import java.io.IOException;

import net.sandrohc.foodie.model.Recipe;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

@Configuration
public class RecipeJob {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private final ReactiveMongoOperations mongoOperations;

	public RecipeJob(JobBuilderFactory jobBuilderFactory,
					 StepBuilderFactory stepBuilderFactory,
					 ReactiveMongoOperations mongoOperations) {

		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.mongoOperations = mongoOperations;
	}

	@Bean
	public Job importRecipeJob(Step stepLoadManager) {
		return jobBuilderFactory.get("load recipes")
				.incrementer(new RunIdIncrementer())
				.flow(stepLoadManager)
				.end()
				.build();
	}

	@Bean
	public Step stepLoadManager(Step stepLoadData) {
		return stepBuilderFactory.get("load data.manager")
				.partitioner("load data", partitioner())
				.step(stepLoadData)
				.taskExecutor(new SimpleAsyncTaskExecutor("load.manager"))
				.build();
	}

	@Bean
	public Step stepLoadData(ItemReader<RecipeJson> reader) {
		return stepBuilderFactory.get("load data")
				.<RecipeJson, Recipe>chunk(1000)
				.reader(reader)
				.processor(new RecipeProcessor())
				.writer(writerRecipes())
				.faultTolerant()
				.skipLimit(Integer.MAX_VALUE)
				.skip(MissingUrlException.class)
				.skip(MissingIdException.class)
				.build();
	}

//	@Bean
//	public Step stepLoadIngredients(ItemReader<Recipe> reader) {
//		return stepBuilderFactory.get("load data")
//				.<Recipe, Collection<Ingredient>>chunk(1000)
//				.reader(reader)
//				.processor(new IngredientProcessor())
//				.writer(writerIngredients())
//				.build();
//	}

	@Bean
	public MultiResourcePartitioner partitioner() {
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources;
		try {
			resources = patternResolver.getResources("classpath:data/*.json");
		} catch (IOException e) {
			throw new RuntimeException("I/O problems when resolving the input file pattern", e);
		}
		partitioner.setResources(resources);
		return partitioner;
	}

	@SuppressWarnings("SpringElInspection")
	@Bean
	@StepScope
	public JsonItemReader<RecipeJson> readerJsonFiles(@Value("#{stepExecutionContext[fileName]}") String filename) {
		final Resource resource;

		if (filename.startsWith("jar:")) {
			resource = new ClassPathResource(filename);
		} else if (filename.startsWith("file:/")) {
			filename = filename.substring("file:/".length());
			resource = new PathResource(filename);
		} else {
			throw new IllegalArgumentException("Invalid filename: " + filename);
		}

		return new JsonItemReaderBuilder<RecipeJson>()
				.jsonObjectReader(new JacksonJsonObjectReader<>(RecipeJson.class))
				.name("recipe json reader")
				.resource(resource)
				.build();
	}

//	@Bean
//	public MongoItemReader<Recipe> readerMongoRecipes() {
//		return new MongoItemReaderBuilder<Recipe>()
////				.template(mongoTemplate)
//				.sorts(new HashMap<>() {{
//					put("_id", Direction.ASC);
//				}})
//				.targetType(Recipe.class)
//				.jsonQuery("{}, { 'ingredients.name': 1 }") // Only fetch the ID and ingredient list
//				.build();
//	}

	@Bean
	public ItemWriter<Recipe> writerRecipes() {
		return items -> mongoOperations.insertAll(items).collectList().block();
	}

//	@Bean
//	public ItemWriter<Collection<Ingredient>> writerIngredients() {
//		return items -> items.forEach(items2 -> mongoOperations.insertAll(items2).collectList().block());
//	}

}