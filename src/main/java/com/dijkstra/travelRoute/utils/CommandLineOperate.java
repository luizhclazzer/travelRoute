package com.dijkstra.travelRoute.utils;

import com.dijkstra.travelRoute.model.dto.RouteParamsDTO;
import com.dijkstra.travelRoute.service.CalculateRouteService;
import com.dijkstra.travelRoute.utils.Exceptions.ExecuteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class CommandLineOperate {

    @Autowired
    CalculateRouteService calculateRouteService;

    public CommandLineOperate() {
    }

    public void Execute() {

        Scanner in = new Scanner(System.in);

        System.out.println("Olá :)");

        while (true) {

            try {

                System.out.println("Por favor, informe a sua rota. Ex.: GRU-CDG. Deixe em branco e tecle enter para sair.");
                RouteParamsDTO routeParamsDTO = UtilValidation.validateRouteInput(in);

                List<String> path = calculateRouteService.cheaperRoute(routeParamsDTO.getFrom().toUpperCase(), routeParamsDTO.getTo().toUpperCase());

                System.out.println("Sua melhor rota: " + path.stream().collect(Collectors.joining(" - ")) + " > $" + calculateRouteService.getCost(routeParamsDTO.getTo().toUpperCase()));

            } catch (ExecuteException ex) {
                System.out.println(ex.getMessage());
            } catch (Throwable ex) {
                System.out.println("Entrada invalida.");
            }
        }

    }
}

