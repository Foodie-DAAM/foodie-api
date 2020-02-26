/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.loader.config;

import java.io.IOException;

import javax.sql.DataSource;

import net.sandrohc.foodie.loader.JobCompletionNotificationListener;
import net.sandrohc.foodie.loader.MissingUrlException;
import net.sandrohc.foodie.loader.model.Recipe;
import net.sandrohc.foodie.loader.model.RecipeJson;
import net.sandrohc.foodie.loader.processor.RecipeItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	public final JobBuilderFactory jobBuilderFactory;
	public final StepBuilderFactory stepBuilderFactory;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public ItemReader<RecipeJson> reader() throws IOException {
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = patternResolver.getResources("classpath:data/*.json");

		JsonItemReader<RecipeJson> jsonItemReader = new JsonItemReaderBuilder<RecipeJson>()
				.jsonObjectReader(new JacksonJsonObjectReader<>(RecipeJson.class))
				.name("recipeJsonItemReader")
				.resource(resources[0])
				.build();

		return new MultiResourceItemReaderBuilder<RecipeJson>()
				.delegate(jsonItemReader)
				.resources(resources)
				.name("multiRecipeJsonItemReader")
				.build();
	}

	@Bean
	public RecipeItemProcessor processor() {
		return new RecipeItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Recipe> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Recipe>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO recipe (id, url, title, description, duration, servings, picture, ingredients, steps, nutrition_facts)" +
					 " VALUES (:id, :url, :title, :description, :duration, :servings, :picture, :ingredients, :steps, :nutritionFacts)" +
					 " ON CONFLICT (url) DO UPDATE SET title = EXCLUDED.title")
				.dataSource(dataSource)
				.build();
	}


	@Bean
	public Job importRecipeJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("importRecipeJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<Recipe> writer) throws IOException {
		return stepBuilderFactory.get("step1")
				.<RecipeJson, Recipe>chunk(50)
				.reader(reader())
				.processor(processor())
				.writer(writer)
				.faultTolerant()
				.skipLimit(Integer.MAX_VALUE)
				.skip(MissingUrlException.class)
				.taskExecutor(new SimpleAsyncTaskExecutor())
				.build();
	}

}