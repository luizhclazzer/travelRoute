package com.dijkstra.travelRoute.batchService;

import com.dijkstra.travelRoute.model.dto.RouteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class RouteItemProcessor implements ItemProcessor<RouteDTO, RouteDTO> {

    private static final Logger log = LoggerFactory.getLogger(RouteItemProcessor.class);

    @Override
    public RouteDTO process(RouteDTO routeDTO) throws Exception {
        final String origin = routeDTO.getOrigin().toUpperCase();
        final String destination = routeDTO.getDestination().toUpperCase();
        final Double cost = routeDTO.getCost();

        final RouteDTO transformed = new RouteDTO(origin, destination, cost);

//        log.info("Converting (" + routeDTO + ") into (" + transformed + ")");

        return transformed;
    }
}
