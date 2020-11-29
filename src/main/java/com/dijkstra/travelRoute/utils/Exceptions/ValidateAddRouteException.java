package com.dijkstra.travelRoute.utils.Exceptions;

public class ValidateAddRouteException extends Exception {

    private static final long serialVersionUID = 1L;

    private String error;

    private Integer status;

    public ValidateAddRouteException(String error, Integer status, String message) {
        super(message);
        this.error = error;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public Integer getStatus() {
        return status;
    }

}
