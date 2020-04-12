package za.co.discovery.assignment.interstella.rest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import za.co.discovery.assignment.interstella.service.DijkstraAlgorithm;

/**
 *
 * @author Muzi Kubeka
 * This delegates the shortest path service to calculate the shortest path
 */
@RestController
public class InterstellaController {


    @Autowired
    @Lazy
    private DijkstraAlgorithm shortestPathService;

    public InterstellaController(DijkstraAlgorithm shortestPathService1){

        this.shortestPathService = shortestPathService1;
    }

    @RequestMapping(method = RequestMethod.GET, value="/shortestpath/{source}/{destination}")
    @ResponseBody
    public String getShortestPath(@PathVariable("source") String source, @PathVariable("destination") String destination){
        return shortestPathService.findShortestPath(source, destination);
    }
}
