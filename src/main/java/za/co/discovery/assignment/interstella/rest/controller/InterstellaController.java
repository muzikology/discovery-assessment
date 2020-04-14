package za.co.discovery.assignment.interstella.rest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.co.discovery.assignment.interstella.entity.Planet;
import za.co.discovery.assignment.interstella.entity.Vertex;
import za.co.discovery.assignment.interstella.helper.Graph;
import za.co.discovery.assignment.interstella.model.ShortestPathModel;
import za.co.discovery.assignment.interstella.repository.PlanetRepository;
import za.co.discovery.assignment.interstella.service.DijkstraAlgorithm;
import za.co.discovery.assignment.interstella.service.PlanetService;
import za.co.discovery.assignment.interstella.service.ShortestPathService;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Muzi Kubeka
 * This delegates the shortest path service to calculate the shortest path
 */
@RestController
@RequestMapping("/interstella")
@CrossOrigin(origins = "http://localhost:4200")
public class InterstellaController {



    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private ShortestPathService shortestPathService;

    public InterstellaController(ShortestPathService shortestPathService1,
                                 PlanetService planetService1, PlanetRepository planetRepository1){

        this.shortestPathService = shortestPathService1;
        this.planetService = planetService1;
        this.planetRepository = planetRepository1;
    }

    @PostMapping("/shortest")
    public String getShortestPath(@Valid @RequestBody ShortestPathModel pathModel,
                                  Model model){
        System.out.println(pathModel.getSelectedVertex());
        System.out.println(pathModel.getSelectedVertexName());
        System.out.println(pathModel.getVertexName());
        System.out.println(pathModel.getDestinationVertex());
        System.out.println(pathModel.getSourceVertex());
        System.out.println(pathModel.getThePath());
        System.out.println(pathModel.getVertexId());
        System.out.println(pathModel.isTrafficAllowed());
        System.out.println(pathModel.isUndirectedGraph());

        StringBuilder path = new StringBuilder();
        Graph graph = planetService.selectGraph();
        if (pathModel.isTrafficAllowed()) {
            graph.setTrafficAllowed(true);
        }
        if (pathModel.isUndirectedGraph()) {
            graph.setUndirectedGraph(true);
        }
        shortestPathService.initializePlanets(graph);

        Vertex source = planetService.getVertexByName(pathModel.getVertexName());
        Vertex destination = planetService.getPlanetById(pathModel.getSelectedVertex());
        shortestPathService.run(source);
        LinkedList<Vertex> paths = shortestPathService.getPath(destination);

        if (paths != null) {
            for (Vertex v : paths) {
                path.append(v.getName() + " (" + v.getVertexId() + ")");
                path.append("\t");
            }
        } else if (source != null && destination != null && source.getVertexId().equals(destination.getVertexId())) {
            path.append("PATH_NOT_NEEDED" + source.getName());
        } else {
            path.append("PATH_NOT_AVAILABLE");
        }
        pathModel.setThePath(path.toString());
        pathModel.setSelectedVertexName(destination.getName());
        model.addAttribute("shortest", pathModel);
        return pathModel.getThePath();
    }

    @GetMapping("/vertices")
    public ResponseEntity<List<Vertex>> getAllPlanets(){

        List<Vertex> planets = planetService.getAllPlanets();
        if(planets.size() == 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There Are No Planets On The DB");
        }
        return new ResponseEntity<>(planets, null, HttpStatus.OK);
    }

    @GetMapping("/vertices/{vertexId}")
    public ResponseEntity<Vertex> getPlanet(@PathVariable ("vertexId") Long vertexId){

        Vertex planet = planetService.getPlanetById(String.valueOf(vertexId));

        return new ResponseEntity<>(planet, null, HttpStatus.OK);
    }

    @PostMapping("vertices")
    public ResponseEntity<Vertex> addNewPlanet(@Valid @RequestBody Vertex planet){

//            if(planetService.vertexExist(planet.getVertexId())){
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Planet Already Exists");
//        }
        return new ResponseEntity<>(planetService.addNewVertex(planet), null, HttpStatus.OK);
    }

    @PutMapping("vertices")
    public ResponseEntity<Vertex> updatePlanet(@Valid @RequestBody Vertex planet){
        Vertex existingPlanet =  planetRepository.findByName(planet.getName());
        if(existingPlanet.getName() != null && (!existingPlanet.getVertexId().equals(planet.getVertexId()))){
            planetService.updatePlanet(planet);

        }

        Vertex updatePlanet = planetService.updatePlanet(planet);

        return new ResponseEntity<>(updatePlanet, null, HttpStatus.OK);
    }

    @DeleteMapping("vertices/{id}")
    public ResponseEntity<Void> deletPlanet(@PathVariable String id) {
        planetService.deleteVertex(id);
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

}
