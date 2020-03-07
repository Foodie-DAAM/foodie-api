/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.batch;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;

import net.sandrohc.foodie.model.Recipe;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RecipeJob {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private final EntityManagerFactory emf;

	public RecipeJob(JobBuilderFactory jobBuilderFactory,
					 StepBuilderFactory stepBuilderFactory,
					 EntityManagerFactory emf) {

		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.emf = emf;
	}

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
					jdbcTemplate.execute("TRUNCATE TABLE recipe CASCADE");
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
	public Step stepLoadData(ItemReader<RecipeJson> reader) {
		return stepBuilderFactory.get("load data")
				.<RecipeJson, Recipe>chunk(100)
				.reader(reader)
				.processor(processor())
				.writer(writer())
				.faultTolerant()
				.skipLimit(Integer.MAX_VALUE)
				.skip(MissingUrlException.class)
				.skip(MissingIdException.class)
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

	@Bean
	public RecipeProcessor processor() {
		return new RecipeProcessor();
	}

	@Bean
	public JpaItemWriter<Recipe> writer() {
		return new JpaItemWriterBuilder<Recipe>()
				.entityManagerFactory(emf)
				.build();
	}

}