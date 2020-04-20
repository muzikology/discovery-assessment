package za.co.discovery.assignment.interstella.rest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.co.discovery.assignment.interstella.entity.Planet;
import za.co.discovery.assignment.interstella.entity.Vertex;
import za.co.discovery.assignment.interstella.helper.Graph;
import za.co.discovery.assignment.interstella.model.ShortestPathModel;
import za.co.discovery.assignment.interstella.repository.PlanetRepository;
import za.co.discovery.assignment.interstella.service.PlanetService;
import za.co.discovery.assignment.interstella.service.ShortestPathService;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Muzi Kubeka
 * This delegates the shortest path service to calculate the shortest path
 */
@Controller
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
    public ResponseEntity<String> getShortestPath(@Valid @RequestBody ShortestPathModel pathModel,
                                  Model model){
        String resultString = null;
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
        resultString = reverseString(pathModel.getThePath());
        return new ResponseEntity<>(resultString, null, HttpStatus.OK);
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

        if(planet.getId() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new Planet Cannot Already have an ID");
        }
        if(planetService.getVertexByName(planet.getName())!= null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not Unique");
        }
        return new ResponseEntity<>(planetService.createVertex(planet), null, HttpStatus.OK);
    }

    @PutMapping("vertices")
    public ResponseEntity<Vertex> updatePlanet(@Valid @RequestBody Vertex planet){

        Vertex updatePlanet = new Vertex();

        updatePlanet = planetService.updateVertex(planet);

        return new ResponseEntity<>(updatePlanet, null, HttpStatus.OK);
    }

    @DeleteMapping("vertices/{id}")
    public ResponseEntity<Void> deletPlanet(@PathVariable String id) {
        planetService.deleteVertex(id);
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

    @RequestMapping("vertex/delete/{vertexId}")
    public String deleteVertex(@PathVariable String vertexId) {
        planetService.deleteVertex(vertexId);
        return "redirect:/vertices";
    }
    @RequestMapping(value = "/shortest", method = RequestMethod.GET)
    public String shortestForm(Model model) {
        ShortestPathModel pathModel = new ShortestPathModel();
        List<Vertex> allVertices = planetService.getAllVertices();
        if (allVertices == null || allVertices.isEmpty()) {
            model.addAttribute("validationMessage", "Planet Not Found");
            return "validation";
        }
        Vertex origin = allVertices.get(0);
        pathModel.setVertexName(origin.getName());
        model.addAttribute("shortest", pathModel);
        model.addAttribute("pathList", allVertices);
        return "shortest";
    }

    @RequestMapping(value = "/verticess", method = RequestMethod.GET)
    public String listVert(Model model) {
        List allVertices = planetService.getAllVertices();
        model.addAttribute("vertices", allVertices);
        return "vertices";
    }

    @RequestMapping("vertex/new")
    public String addVertex(Model model) {
        model.addAttribute("vertex", new Vertex());
        return "vertexadd";
    }

    @RequestMapping(value = "vertex", method = RequestMethod.POST)
    public String saveVertex(Vertex vertex, Model model) {
        if (planetService.vertexExist(vertex.getVertexId())) {
            buildVertexValidation(vertex.getVertexId(), model);
            return "validation";
        }
        planetService.saveVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }
    public void buildVertexValidation(String vertexId, Model model) {
        String vertexName = planetService.getPlanetById(vertexId) == null ? "" : planetService.getPlanetById(vertexId).getName();
        String message = "Planet " + vertexId + " already exists as " + vertexName;
        model.addAttribute("validationMessage", message);
    }

    @RequestMapping("vertex/edit/{vertexId}")
    public String editVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", planetService.getPlanetById(vertexId));
        return "vertexupdate";
    }

    @RequestMapping("vertex/{vertexId}")
    public String showVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", planetService.getPlanetById(vertexId));
        return "vertexshow";
    }

    @RequestMapping(value = "vertexupdate", method = RequestMethod.POST)
    public String updateVertex(Vertex vertex) {
        planetService.updateVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }


    public String reverseString(String str)
    {

        Pattern pattern = Pattern.compile("\\s");

        String[] temp = pattern.split(str);
        String result = "";

        // Iterate over the temp array and store
        // the string in reverse order.
        for (int i = 0; i < temp.length; i++) {
            if (i == temp.length - 1)
                result = temp[i] + result;
            else
                result = " " + temp[i] + result;
        }
        return result;
    }


}