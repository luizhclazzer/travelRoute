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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/routes")
@Api(value="bestRoute", description="Get a best route, add a new a route, get route by Id and Delete routes")
public class CalculateRouteRestController {

    @Autowired
    CalculateRouteService calculateRouteService;

    @Autowired
    RouteService routeService;

    @Autowired
    RouteRepository routeRepository;

    @ApiOperation(value = "Best route by origin and destination params")
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

    @ApiOperation(value = "Register a new route")
    @PostMapping
    public ResponseEntity<RouteDTO> addRoute(@RequestBody RouteDTO routeDTO, UriComponentsBuilder uriBuilder)
            throws ValidateAddRouteException, FileException {

        try {
            Route route = routeService.addRoute(routeDTO);

            String line = route.getOrigin().toUpperCase() + UtilFile.CSV_SEPARATOR +
                    route.getDestination().toUpperCase() + UtilFile.CSV_SEPARATOR +
                    route.getCost();
            UtilFile.writeLineToCSVFile(line);

            URI uri = uriBuilder.path("/routes/{id}").buildAndExpand(route.getId()).toUri();
            return ResponseEntity.created(uri).body(new RouteDTO(route.getOrigin(), route.getDestination(), route.getCost()));

        } catch (ValidateAddRouteException ex) {
            return ResponseEntity.status(ex.getStatus()).build();
        }

    }

    @ApiOperation(value = "Get route by Id param")
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

    @ApiOperation(value = "Delete route by origin and destination params")
    @DeleteMapping("/origin/{origin}/destination/{destination}")
    public ResponseEntity<?> delete(@PathVariable("origin") String origin,
                                    @PathVariable("destination") String destination) throws IOException, FileException {
        Optional<Route> route = routeRepository.findByOriginAndDestination(origin.toUpperCase(), destination.toUpperCase());
        if (route.isPresent()) {
            routeRepository.deleteById(route.get().getId());

            writeDataFromDBToCSVFile();

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    public void writeDataFromDBToCSVFile() throws FileException, IOException {

        UtilFile.emptyCSVFile();

        List<Route> routes = routeRepository.findAll();
        routes.forEach(route -> {
            String line = route.getOrigin() + UtilFile.CSV_SEPARATOR + route.getDestination() + UtilFile.CSV_SEPARATOR + route.getCost();
            try {
                UtilFile.writeLineToCSVFile(line);
            } catch (FileException e) {
                e.printStackTrace();
            }
        });

    }
}
