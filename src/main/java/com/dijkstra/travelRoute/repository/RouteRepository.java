package com.dijkstra.travelRoute.repository;

import com.dijkstra.travelRoute.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByOriginAndDestination(String origin, String destination);

}
