package com.dijkstra.travelRoute.service;

import com.dijkstra.travelRoute.model.Route;
import com.dijkstra.travelRoute.repository.RouteRepository;
import com.dijkstra.travelRoute.utils.Exceptions.ExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import  static java.util.Objects.isNull;

@Service
public class CalculateRouteService {

    private static Logger LOG = LoggerFactory.getLogger(CalculateRouteService.class);

    @Autowired
    private ApplicationArguments applicationArguments;

    @Autowired
    private RouteRepository routeRepository;


    private List<Route> routes;
    private Set<String> settledNodes;
    private Set<String> unSettledNodes;
    private Map<String, String> predecessors;
    private Map<String, Double> distance;

    /**
     * O algorítimo utilizado utilizada o método "existe aberto, ou "set", o qual é mais didádico, porém menos otimizado
     * que o método de "fila de prioridade"
     */
    public List<String> cheaperRoute(String source, String target) {

        initialization(source);

        /*Checa se existe a rota, independente de quantas escalas*/
        if (predecessors.get(target) == null) {
            throw new ExecuteException("Nenhuma rota encontrada :(", HttpStatus.NOT_FOUND);
        }

        List<String> path = new ArrayList<>();
        String step = target;
        path.add(step);

        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }

        /**
        * Reverte a string porque a busca vem de "trás para frente", ou seja,
        * é guardado sempre o predecessor de cada node
        */
        Collections.reverse(path);

        return path;
    }

    /**
     * initialization faz o mapeamento de todas as ramificações possíveis partindo do @param source, sendo que
     * findMinimalDistances adiciona os nós de destido de @param source a cada menor distância encontrada em
     * unSettledNodes, o que faz com que dos os destinos também sejam mapeados com a menor distância,
     * assim como o destino dos destinos, e assim por diante.
     */
    public void initialization(String source) {

        this.routes = routeRepository.findAll();

        if (isNull(this.routes) || this.routes.isEmpty()) {
            throw new ExecuteException("Não foi encontrada nenhuma rota no banco de dados.", HttpStatus.NO_CONTENT);
        }

        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        distance.put(source, 0.0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            String node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    /**
    * findMinimalDistances compara a menor distância (entre o @param node e cada adjacente seu) calculada até então com
    * a recém-calculada. Caso o novo caminho encontrado seja menor, assume esse caminho como menor distância, alimenta a
    * lista de predecessores e adiciona o destino na lista dos não resolvidos. Depois, segue fazendo o mesmo para os
    * outros adjacentes, adicionando cada um na lista dos não resolvidos. Portanto, os nós que não são destino de
    * @param node não são resolvidos nessa chamada.
     */
    private void findMinimalDistances(String node) {
        List<String> adjacentNodes = getNeighbors(node);
        adjacentNodes.forEach(target -> {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        });
    }

    /**
    * getDistance apenas encontra a distância entre o primeiro e o segundo parâmetro passados
     */
    private Double getDistance(String node, String target) {
        return routes.stream().filter(route -> route.getOrigin().equals(node) && route.getDestination().equals(target))
                .map(route -> route.getCost()).findFirst().orElseThrow(() -> new ExecuteException("Source/target not found"));
    }

    /**
     *Busca todos os nós na adjacência (vizinhos) de @param node
     */
    private List<String> getNeighbors(String node) {
        return routes.stream().filter(route -> route.getOrigin().equals(node) && !isSettled(route.getDestination()))
                .map(route -> route.getDestination()).collect(Collectors.toList());
    }

    /**
     * getCost apenas retorna o custo para para o destino passado em @param target. "Distance" contém todos os destinos
     * possíveis da origem, com seus respectivos valores.
     */
    public Double getCost(String target) {
        return distance.getOrDefault(target, 0.0);
    }

    /**
    * getMinimum recebe no @param vertexes uma lista de nodes e verifica qual dentre eles possui a menor distância para
    * o seu próximo adjacente. É utilizado para o algorítimo saber, dentre os vértices abertos, qual o próximo que será
    * selecionado para fechar
    */
    private String getMinimum(Set<String> vertexes) {
        String minimum = vertexes.iterator().next();
        for (String vertex : vertexes) {
            if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                minimum = vertex;
            }
        }
        return minimum;
    }

    /**
    * isSettled indica se o vértice está resolvido, ou seja, já foi mapeado.
    */
    private boolean isSettled(String vertex) {
        return settledNodes.contains(vertex);
    }

    private Double getShortestDistance(String destination) {
        return distance.getOrDefault(destination, Double.MAX_VALUE);
    }

}
