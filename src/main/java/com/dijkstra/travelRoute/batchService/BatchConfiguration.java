/*
 * Based on official spring batch-processing documentation
 * https://spring.io/guides/gs/batch-processing/
 */
package com.dijkstra.travelRoute.batchService;

import com.dijkstra.travelRoute.model.dto.RouteDTO;
import com.dijkstra.travelRoute.utils.UtilValidation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.*;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    ApplicationArguments applicationArguments;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<RouteDTO> reader() {
        FlatFileItemReader<RouteDTO> reader = new FlatFileItemReader<>();
        //reader.setResource(new PathResource(applicationArguments.getNonOptionArgs().get(0)));
        if (UtilValidation.isJUnitTest()) {
            reader.setResource(new ClassPathResource("routes.csv"));
        } else {
            reader.setResource(new PathResource(System.getProperty("param")));
        }
        reader.setLineMapper(new DefaultLineMapper<RouteDTO>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    { setNames(new String[]{"origin", "destination", "cost"}); }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<RouteDTO>() {
                    { setTargetType(RouteDTO.class); }
                });
            }
        });
        return reader;
    }

    @Bean
    public RouteItemProcessor processor() {
        return new RouteItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<RouteDTO> writer() {
        JdbcBatchItemWriter<RouteDTO> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO route (origin, destination, cost) VALUES (:origin, :destination, :cost)");
        writer.setDataSource(this.dataSource);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importRouteJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importRouteJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<RouteDTO, RouteDTO>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    // end::jobstep[]

}
