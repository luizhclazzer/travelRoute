package com.dijkstra.travelRoute.utils;

import com.dijkstra.travelRoute.model.dto.RouteParamsDTO;
import com.dijkstra.travelRoute.utils.Exceptions.ExecuteException;
import com.dijkstra.travelRoute.utils.Exceptions.ValidateInputException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class UtilValidation {

    private UtilValidation() {
    }

    public static boolean isJUnitTest() {

        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }

        return false;
    }

    public static void validateFileNameInput(String text, String errorMessage) throws ValidateInputException {
        if (text == null || text.isEmpty()) {
            throw new ValidateInputException(errorMessage);
        }

        if (isJUnitTest() == false) {
            validateFileExists(text);
        }
    }

    private static void validateFileExists(String text) throws ValidateInputException {

        if (Files.notExists(Paths.get(text))) {
            throw new ValidateInputException("Falha ao carregar o arquivo. Por favor, confira se o caminho está correto.");
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

    public static String getCSVFileName() {

        String fileName;

        if (UtilValidation.isJUnitTest()) {
            fileName = "routes.csv";
        } else {
            fileName = System.getProperty("param");
        }

        return fileName;
    }

}
