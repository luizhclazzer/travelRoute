package com.dijkstra.travelRoute.utils.Exceptions;

import org.springframework.http.HttpStatus;

public class ExecuteException extends RuntimeException {

    private HttpStatus status;

    public ExecuteException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ExecuteException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
