package com.dijkstra.travelRoute.service;

import com.dijkstra.travelRoute.model.Route;
import com.dijkstra.travelRoute.model.dto.RouteDTO;
import com.dijkstra.travelRoute.repository.RouteRepository;
import com.dijkstra.travelRoute.utils.Exceptions.ExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

import  static java.util.Objects.isNull;
import  static java.util.Objects.nonNull;

@Service
public class CalculateRouteService {

    private static Logger LOG = LoggerFactory.getLogger(CalculateRouteService.class);

    @Autowired
    private ApplicationArguments applicationArguments;

    @Autowired
    private RouteRepository routeRepository;


    private List<Route> routes;
    private Set<String> settledNodes;
    private Set<String> unSettledNodes;
    private Map<String, String> predecessors;
    private Map<String, Double> distance;

    public List<String> cheaperRoute(String source, String target) {

        init(source);

        if (predecessors.get(target) == null) {
            throw new ExecuteException("Nenhuma rota encontrada :(", HttpStatus.NOT_FOUND);
        }

        List<String> path = new ArrayList<>();
        String step = target;
        path.add(step);

        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }

        Collections.reverse(path);

        return path;
    }

    public void init(String source) {

        this.routes = routeRepository.findAll();

        if (isNull(this.routes) || this.routes.isEmpty()) {
            throw new ExecuteException("Não foi encontrada nenhuma rota no banco de dados.", HttpStatus.NO_CONTENT);
        }

        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        distance.put(source, 0.0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            String node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(String node) {
        List<String> adjacentNodes = getNeighbors(node);
        adjacentNodes.forEach(target -> {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        });
    }

    private Double getDistance(String node, String target) {
        return routes.stream().filter(route -> route.getOrigin().equals(node) && route.getDestination().equals(target))
                .map(route -> route.getCost()).findFirst().orElseThrow(() -> new ExecuteException("Source/target not found"));
    }

    private List<String> getNeighbors(String node) {
        return routes.stream().filter(route -> route.getOrigin().equals(node) && !isSettled(route.getDestination()))
                .map(route -> route.getDestination()).collect(Collectors.toList());
    }

    public Double getCost(String target) {
        return distance.getOrDefault(target, 0.0);
    }

    private String getMinimum(Set<String> vertexes) {
        String minimum = vertexes.iterator().next();
        for (String vertex : vertexes) {
            if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                minimum = vertex;
            }
        }
        return minimum;
    }

    private boolean isSettled(String vertex) {
        return settledNodes.contains(vertex);
    }

    private Double getShortestDistance(String destination) {
        return distance.getOrDefault(destination, Double.MAX_VALUE);
    }

}
