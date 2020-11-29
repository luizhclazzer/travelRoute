package com.dijkstra.travelRoute.model.dto;

public class BestRouteDTO {

    private String path;
    private Double cost;

    public String getPath() {
        return path;
    }

    public BestRouteDTO(String path, Double cost) {
        this.path = path;
        this.cost = cost;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
