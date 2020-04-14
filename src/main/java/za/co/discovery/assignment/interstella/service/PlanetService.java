package za.co.discovery.assignment.interstella.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.interstella.entity.Edge;
import za.co.discovery.assignment.interstella.entity.Traffic;
import za.co.discovery.assignment.interstella.entity.Vertex;
import za.co.discovery.assignment.interstella.helper.Graph;
import za.co.discovery.assignment.interstella.repository.EdgeRepository;
import za.co.discovery.assignment.interstella.repository.PlanetRepository;
import za.co.discovery.assignment.interstella.repository.TrafficRepository;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final EdgeRepository edgeRepository;
    private final TrafficRepository trafficRepository;

    public PlanetService(PlanetRepository planetRepository1,
                         EdgeRepository edgeRepository1,
                         TrafficRepository trafficRepository1){
        this.planetRepository = planetRepository1;
        this.edgeRepository = edgeRepository1;
        this.trafficRepository = trafficRepository1;

    }

    @Transactional(readOnly = true)
    public List<Vertex> getAllPlanets() {
        return planetRepository.findAll();
    }

    @Transactional
    public Optional<Vertex> getPlanetByName(String name){

        return planetRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Vertex getPlanetById(String id) {
        return planetRepository.getPlanetByVertexId(id);
    }

    public Vertex addNewVertex(Vertex planet)
    {
        Vertex addPlanet = planetRepository.save(planet);
        return addPlanet;
    }

    public Vertex updatePlanet(Vertex planet){
//        if(planet.getVertexId()== null){
//            return addNewVertex(planet);
//        }

        return planetRepository.save(planet);
    }

    public Graph selectGraph() {
        List<Vertex> vertices = planetRepository.findAll();
        List<Edge> edges = edgeRepository.findAll();
        List<Traffic> traffics = trafficRepository.findAll();

        Graph graph = new Graph(vertices, edges, traffics);

        return graph;
    }

    public Boolean vertexExist(String id){

        boolean exist = planetRepository.existsByVertexId(id);

        return exist;
    }

    public void deletePlanetById(Long id){

        planetRepository.deleteById(id);

    }

    public void persistGraph() {
        URL resource = getClass().getResource("/interstellar.xlsx");
        File file1;
        try {
            file1 = new File(resource.toURI());
            persistGraph(file1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void persistGraph(File file) {
        XLSXHandler handler = new XLSXHandler(file);

        List<Vertex> vertices = handler.readVertexes();
        if (vertices != null && !vertices.isEmpty()) {
            for (Vertex v : vertices) {
                planetRepository.save(v);
            }
        }
        List<Edge> edges = handler.readEdges();
        if (edges != null && !edges.isEmpty()) {
            for (Edge e : edges) {
                edgeRepository.save(e);
            }
        }
        List<Traffic> traffic = handler.readTraffics();
        if (edges != null && !edges.isEmpty()) {
            for (Traffic t : traffic) {
                trafficRepository.save(t);
            }
        }
    }


    public Vertex saveVertex(Vertex vertex) {
        planetRepository.save(vertex);
        return vertex;
    }

    public Vertex updateVertex(Vertex vertex) {
        if(vertex.getVertexId() != null) {
            return planetRepository.save(vertex);
        }

        return vertex;
    }

    public boolean deleteVertex(String vertexId) {
        planetRepository.deleteByVertexId(vertexId);
        return true;
    }

    public List<Vertex> getAllVertices() {
        return planetRepository.findAll();
    }

    public Vertex getVertexByName(String name) {
        return planetRepository.findByName(name).get();
    }

    public Vertex getVertexById(String vertexId) {
        return planetRepository.getPlanetByVertexId(vertexId);
    }

    public Edge saveEdge(Edge edge) {
        edgeRepository.save(edge);
        return edge;
    }

    public Edge updateEdge(Edge edge) {
        if(edge.getEdgeId() != null){
            return edgeRepository.save(edge);
        }
        return edgeRepository.save(edge);
    }

    public boolean deleteEdge(long recordId) {
        deletePlanetById(recordId);
        return true;
    }

    public List<Edge> getAllEdges() {
        return edgeRepository.findAll();
    }

    public Edge getEdgeById(long recordId) {
        return edgeRepository.getOne(recordId);
    }



    public Traffic saveTraffic(Traffic traffic) {
        trafficRepository.save(traffic);
        return traffic;
    }

    public Traffic updateTraffic(Traffic traffic) {
        if(traffic.getRouteId() != null) {
            return trafficRepository.save(traffic);
        }
        return traffic;
    }

    public boolean deleteTraffic(String routeId) {
        trafficRepository.deleteById(Long.valueOf(routeId));
        return true;
    }

    public List<Traffic> getAllTraffics() {
        return trafficRepository.findAll();
    }

    public Traffic getTrafficById(String routeId) {
        return trafficRepository.getOne(Long.valueOf(routeId));
    }

}
