package com.dijkstra.travelRoute.model.dto;


public class RouteDTO {

    private String origin;

    private String destination;

    private Double cost;

    public RouteDTO() {
    }

    public RouteDTO(String origin, String destination, Double cost) {
        this.origin = origin;
        this.destination = destination;
        this.cost = cost;
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
