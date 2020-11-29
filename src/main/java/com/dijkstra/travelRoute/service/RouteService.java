package com.dijkstra.travelRoute.service;

import com.dijkstra.travelRoute.model.Route;
import com.dijkstra.travelRoute.model.dto.RouteDTO;
import com.dijkstra.travelRoute.repository.RouteRepository;
import com.dijkstra.travelRoute.utils.Exceptions.ValidateAddRouteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RouteService {

    private static String csvFile;

    public static String getCsvFile() {
        return csvFile;
    }

    public static void setCsvFile(String csvFile) {
        RouteService.csvFile = csvFile;
    }

    @Autowired
    private RouteRepository routeRepository;

    public Route addRoute(RouteDTO routeDTO) throws ValidateAddRouteException {

        routeValidation(routeDTO);
        Route route = new Route(routeDTO.getOrigin(), routeDTO.getDestination(), routeDTO.getCost());

        routeRepository.save(route);

        return route;
    }

    private void routeValidation(RouteDTO routeDTO) throws ValidateAddRouteException {
        Optional<Route> route = routeRepository.findByOriginAndDestination(routeDTO.getOrigin(), routeDTO.getDestination());
        if (route.isPresent()) {
            throw new ValidateAddRouteException("Conflict", 409, "This route is already registered.");
        }
    }
}
