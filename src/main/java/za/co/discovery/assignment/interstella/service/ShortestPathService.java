package za.co.discovery.assignment.interstella.service;

import org.springframework.stereotype.Service;
import za.co.discovery.assignment.interstella.entity.Edge;
import za.co.discovery.assignment.interstella.entity.Vertex;
import za.co.discovery.assignment.interstella.helper.Graph;

import java.util.*;


@Service
public class ShortestPathService {

    private List<Vertex> vertices;
    private List<Edge> edges;
    private Set<Vertex> visitedVertices;
    private Set<Vertex> unvisitedVertices;
    private Map<Vertex, Vertex> previousPaths;
    private Map<Vertex, Float> distance;

    public ShortestPathService() {
    }

    public ShortestPathService(Graph graph) {
        this.vertices = new ArrayList<>(graph.getVertexes());
        if (graph.isTrafficAllowed()) {
            graph.processTraffics();
        }
        if (graph.isUndirectedGraph()) {
            this.edges = new ArrayList<>(graph.getUndirectedEdges());
        } else {
            this.edges = new ArrayList<>(graph.getEdges());
        }
    }

    public void initializePlanets(Graph graph) {
        this.vertices = new ArrayList<>(graph.getVertexes());
        if (graph.isTrafficAllowed()) {
            graph.processTraffics();
        }
        if (graph.isUndirectedGraph()) {
            this.edges = new ArrayList<>(graph.getUndirectedEdges());
        } else {
            this.edges = new ArrayList<>(graph.getEdges());
        }
    }

    public void run(Vertex source) {
        distance = new HashMap<>();
        previousPaths = new HashMap<>();
        visitedVertices = new HashSet<>();
        unvisitedVertices = new HashSet<>();
        distance.put(source, 0f);
        unvisitedVertices.add(source);
        while (unvisitedVertices.size() > 0) {
            Vertex currentVertex = getVertexWithLowestDistance(unvisitedVertices);
            visitedVertices.add(currentVertex);
            unvisitedVertices.remove(currentVertex);
            evaluateNeighborsWithMinimalDistances(currentVertex);
        }
    }

    private Vertex getVertexWithLowestDistance(Set<Vertex> vertexes) {
        Vertex lowestVertex = null;
        for (Vertex vertex : vertexes) {
            if (lowestVertex == null) {
                lowestVertex = vertex;
            } else if (getShortestDistance(vertex) < getShortestDistance(lowestVertex)) {
                lowestVertex = vertex;
            }
        }
        return lowestVertex;
    }

    private void evaluateNeighborsWithMinimalDistances(Vertex currentVertex) {
        List<Vertex> adjacentVertices = getNeighbors(currentVertex);
        for (Vertex target : adjacentVertices) {
            float alternateDistance = getShortestDistance(currentVertex) + getDistance(currentVertex, target);
            if (alternateDistance < getShortestDistance(target)) {
                distance.put(target, alternateDistance);
                previousPaths.put(target, currentVertex);
                unvisitedVertices.add(target);
            }
        }
    }

    private List<Vertex> getNeighbors(Vertex currentVertex) {
        List<Vertex> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            Vertex destination = fromId(edge.getDestination());
            if (edge.getSource().equals(currentVertex.getVertexId()) && !isVisited(destination)) {
                neighbors.add(destination);
            }
        }
        return neighbors;
    }

    public Vertex fromId(final String str) {
        for (Vertex v : vertices) {
            if (v.getVertexId().equalsIgnoreCase(str)) {
                return v;
            }
        }
        Vertex islandVertex = new Vertex();
        islandVertex.setVertexId(str);
        islandVertex.setName("Island " + str);
        return islandVertex;
    }

    private boolean isVisited(Vertex vertex) {
        return visitedVertices.contains(vertex);
    }

    private Float getShortestDistance(Vertex destination) {
        Float d = distance.get(destination);
        if (d == null) {
            return Float.POSITIVE_INFINITY;
        } else {
            return d;
        }
    }

    private float getDistance(Vertex source, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(source.getVertexId()) && edge.getDestination().equals(target.getVertexId())) {
                return edge.getDistance() + edge.getTimeDelay();
            }
        }
        throw new RuntimeException("Error: Something went wrong!");
    }

    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<>();
        Vertex step = target;

        if (previousPaths.get(step) == null) {
            return null;
        }
        path.add(step);
        while (previousPaths.get(step) != null) {
            step = previousPaths.get(step);
            path.add(step);
        }

        Collections.reverse(path);
        return path;
    }

}
