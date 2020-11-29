package com.dijkstra.travelRoute.model;

import com.dijkstra.travelRoute.model.dto.RouteDTO;

import javax.persistence.*;

@Entity
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origin;
    private String destination;
    private Double cost;

    @Deprecated
    public Route() {
    }

    public Route(String origin, String destination, Double cost) {
        this.origin = origin;
        this.destination = destination;
        this.cost = cost;
    }

    public static Route from(RouteDTO routeDTO) {

        return new Route(routeDTO.getOrigin(), routeDTO.getDestination(), routeDTO.getCost());

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
