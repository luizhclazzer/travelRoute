package com.dijkstra.travelRoute.controller;

import com.dijkstra.travelRoute.model.Route;
import com.dijkstra.travelRoute.model.dto.BestRouteDTO;
import com.dijkstra.travelRoute.model.dto.RouteDTO;
import com.dijkstra.travelRoute.repository.RouteRepository;
import com.dijkstra.travelRoute.service.CalculateRouteService;
import com.dijkstra.travelRoute.service.RouteService;
import com.dijkstra.travelRoute.utils.Exceptions.ExecuteException;
import com.dijkstra.travelRoute.utils.Exceptions.FileException;
import com.dijkstra.travelRoute.utils.Exceptions.ValidateAddRouteException;
import com.dijkstra.travelRoute.utils.UtilFile;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/routes")
public class CalculateRouteRestController {

    @Autowired
    CalculateRouteService calculateRouteService;

    @Autowired
    RouteService routeService;

    @Autowired
    RouteRepository routeRepository;

    @GetMapping("/origin/{origin}/destination/{destination}")
    public ResponseEntity<BestRouteDTO> bestRoute(@PathVariable("origin") String origin,
                                                  @PathVariable("destination") String destination) throws ExecuteException {

        try {

            List<String> pathRoute = calculateRouteService.cheaperRoute(origin.toUpperCase(), destination.toUpperCase());

            return ResponseEntity.ok(
                    new BestRouteDTO(
                            pathRoute.stream().collect(Collectors.joining(" - ")),
                            calculateRouteService.getCost(destination.toUpperCase())));

        } catch (ExecuteException ex) {
            return ResponseEntity.status(ex.getStatus()).build();
        }

    }

    @PostMapping
    public ResponseEntity<RouteDTO> addRoute(@RequestBody RouteDTO routeDTO, UriComponentsBuilder uriBuilder)
            throws ValidateAddRouteException, FileException {

        try {
            Route route = routeService.addRoute(routeDTO);

            String line = route.getOrigin() + UtilFile.CSV_SEPARATOR + route.getDestination() + UtilFile.CSV_SEPARATOR + route.getCost();
            UtilFile.writeLineToCSVFile(line);

            URI uri = uriBuilder.path("/routes/{id}").buildAndExpand(route.getId()).toUri();
            return ResponseEntity.created(uri).body(new RouteDTO(route.getOrigin(), route.getDestination(), route.getCost()));

        } catch (ValidateAddRouteException ex) {
            return ResponseEntity.status(ex.getStatus()).build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> routeByID(@PathVariable Long id) {
        Optional<Route> route = routeRepository.findById(id);
        if (route.isPresent()) {
            return ResponseEntity.ok(new RouteDTO(route.get().getOrigin(),
                    route.get().getDestination(),
                    route.get().getCost()));
        }

        return ResponseEntity.notFound().build();
    }
}