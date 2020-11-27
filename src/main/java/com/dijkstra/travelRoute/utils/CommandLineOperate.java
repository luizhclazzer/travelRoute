package com.dijkstra.travelRoute.utils;

import java.util.Scanner;

public class CommandLineOperate {

    public CommandLineOperate() {

        Scanner in = new Scanner(System.in);

        System.out.println("Hello!");
        System.out.println("Please enter your route. E.g. GRU-CDG. Blank to exit.");

        while (true) {

            try {

                if (in.toString().isEmpty()) {
                    System.exit(0);
                }

            } catch (Throwable ex) {
                System.out.println("invalid input!");
            }
        }
    }
}

