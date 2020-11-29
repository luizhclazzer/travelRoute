package com.dijkstra.travelRoute.utils;

import com.dijkstra.travelRoute.model.dto.RouteParamsDTO;
import com.dijkstra.travelRoute.utils.Exceptions.ExecuteException;
import com.dijkstra.travelRoute.utils.Exceptions.ValidateInputException;

import java.util.Scanner;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class UtilValidation {

    private UtilValidation() {
    }

    public static void validateFileNameInput(String text, String errorMessage) throws ValidateInputException {
        if (text == null || text.isEmpty()) {
            throw new ValidateInputException(errorMessage);
        }
    }

    public static RouteParamsDTO validateRouteInput(Scanner in) {

        String line = in.nextLine().trim();

        if (line.isEmpty()) {
            System.exit(0);
        }

        String[] route = line.split("-");
        if (route.length != 2) {
            ThrowParamsException();
        }
        String from = route[0];
        String to = route[1];

        if (from.isEmpty() || isNull(from) || to.isEmpty() || isNull(to)) {
            ThrowParamsException();
        }

        return new RouteParamsDTO(route[0], route[1]);
    }

    private static void ThrowParamsException() {

        throw new ExecuteException("Informe uma rota válida de acordo com exemplo.");

    }

}
