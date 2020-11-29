package com.dijkstra.travelRoute.batchService;

import com.dijkstra.travelRoute.model.dto.RouteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
//            log.info("JOB FINISHED! Time to verify the results");

            List<RouteDTO> results = this.jdbcTemplate.query("SELECT origin, destination, cost FROM route",
                    (rs, row) -> new RouteDTO(rs.getString(1), rs.getString(2), rs.getDouble(3)));

//            for (RouteDTO routeDTO : results) {
//                log.info("Found <" + routeDTO.toString() + "> in the database.");
//            }

        }
    }

}
