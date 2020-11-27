package com.dijkstra.travelRoute.utils;

import com.dijkstra.travelRoute.model.dto.RouteParamsDTO;
import com.dijkstra.travelRoute.utils.Exceptions.ExecuteException;

import java.util.Scanner;

public class CommandLineOperate {

    public CommandLineOperate() {

        Scanner in = new Scanner(System.in);

        System.out.println("Hello!");

        while (true) {

            try {

                System.out.println("Please enter your route. E.g. GRU-CDG. Blank to exit.");
                RouteParamsDTO routeParamsDTO = UtilValidation.validateRouteInput(in);

            } catch (ExecuteException ex) {
                System.out.println(ex.getMessage());
            } catch (Throwable ex) {
                System.out.println("invalid input!");
            }
        }
    }
}

