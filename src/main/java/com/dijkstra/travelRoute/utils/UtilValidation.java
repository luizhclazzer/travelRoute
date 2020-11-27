package com.dijkstra.travelRoute.utils;

import com.dijkstra.travelRoute.utils.Exceptions.ValidateInputException;

public class UtilValidation {

    private UtilValidation() {
    }

    public static void validateInput(String text, String errorMessage) throws ValidateInputException {
        if (text == null || text.isEmpty()) {
            throw new ValidateInputException(errorMessage);
        }
    }

}
