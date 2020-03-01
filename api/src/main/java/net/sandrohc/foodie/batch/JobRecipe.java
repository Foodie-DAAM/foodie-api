/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.batch;

import java.io.IOException;

import javax.sql.DataSource;

import net.sandrohc.foodie.model.Recipe;
import net.sandrohc.foodie.batch.model.RecipeJson;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JobRecipe {

	public final JobBuilderFactory jobBuilderFactory;
	public final StepBuilderFactory stepBuilderFactory;

	public JobRecipe(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

//	@Bean({"recipeJob"})
	@Bean
	public Job importRecipeJob(Step stepClearDb, Step stepLoadManager) {
		return jobBuilderFactory.get("load recipes")
				.incrementer(new RunIdIncrementer())
				.flow(stepClearDb)
				.next(stepLoadManager)
				.end()
				.build();
	}

	@Bean
	public Step stepClearDb(JdbcTemplate jdbcTemplate) {
		return stepBuilderFactory.get("clear db")
				.tasklet((contribution, chunkContext) -> {
					jdbcTemplate.execute("TRUNCATE TABLE recipe");
					return RepeatStatus.FINISHED;
				})
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
	public Step stepLoadData(ItemReader<RecipeJson> reader, JdbcBatchItemWriter<Recipe> writer) {
		return stepBuilderFactory.get("load data")
				.<RecipeJson, Recipe>chunk(500)
				.reader(reader)
				.processor(processor())
				.writer(writer)
				.faultTolerant()
				.skipLimit(Integer.MAX_VALUE)
				.skip(MissingUrlException.class)
				.build();
	}

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
	public JsonItemReader<RecipeJson> reader(@Value("#{stepExecutionContext[fileName]}") String filename) {
		int index = filename.indexOf(":");
		if (index != -1) {
			filename = filename.substring(index + 2);
		}

		return new JsonItemReaderBuilder<RecipeJson>()
				.jsonObjectReader(new JacksonJsonObjectReader<>(RecipeJson.class))
				.name("recipe json reader")
				.resource(new PathResource(filename))
//				.resource(new ClassPathResource(filename))
//				.resource(new FileSystemResource(filename))
				.build();
	}

	@Bean
	public RecipeProcessor processor() {
		return new RecipeProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Recipe> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Recipe>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO recipe (id, url, title, description, duration, servings, picture, ingredients, steps, nutrition_facts)" +
					 " VALUES (:id, :url, :title, :description, :duration, :servings, :picture, :ingredients, :steps, :nutritionFacts)"
				)
				.dataSource(dataSource)
				.build();
	}

}