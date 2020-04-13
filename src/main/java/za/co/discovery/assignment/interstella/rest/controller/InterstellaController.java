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
public class InterstellaController {


//    @Autowired
//    @Lazy
//    private DijkstraAlgorithm shortestPathService;

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
        StringBuilder path = new StringBuilder();
        Graph graph = planetService.selectGraph();
        if (pathModel.isTrafficAllowed()) {
            graph.setTrafficAllowed(true);
        }
        if (pathModel.isUndirectedGraph()) {
            graph.setUndirectedGraph(true);
        }
        shortestPathService.initializePlanets(graph);

        Optional<Vertex> source = planetService.getPlanetByName(pathModel.getVertexName());
        Vertex destination = planetService.getPlanetById(pathModel.getSelectedVertex());
        shortestPathService.run(source.get());
        LinkedList<Vertex> paths = shortestPathService.getPath(destination);

        if (paths != null) {
            for (Vertex v : paths) {
                path.append(v.getName() + " (" + v.getVertexId() + ")");
                path.append("\t");
            }
        } else if (source != null && destination != null && source.get().getVertexId().equals(destination.getVertexId())) {
            path.append("PATH_NOT_NEEDED" + source.get().getName());
        } else {
            path.append("PATH_NOT_AVAILABLE");
        }
        pathModel.setThePath(path.toString());
        pathModel.setSelectedVertexName(destination.getName());
        model.addAttribute("shortest", pathModel);
        return "result";
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

            if(planetService.vertexExist(planet.getVertexId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Planet Already Exists");
        }
        return new ResponseEntity<>(planetService.addNewVertex(planet), null, HttpStatus.OK);
    }

    @PutMapping("vertices")
    public ResponseEntity<Vertex> updatePlanet(@Valid @RequestBody Vertex planet){
        Optional<Vertex> existingPlanet =  planetRepository.findByName(planet.getName());
        if(existingPlanet.isPresent() && (!existingPlanet.get().getVertexId().equals(planet.getVertexId()))){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Planet Already Used");
        }

        Vertex updatePlanet = planetService.updatePlanet(planet);

        return new ResponseEntity<>(updatePlanet, null, HttpStatus.OK);
    }

    @DeleteMapping("vertices/{id}")
    public ResponseEntity<Void> deletPlanet(@PathVariable Long id) {
        planetService.deletePlanetById(id);
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

}
