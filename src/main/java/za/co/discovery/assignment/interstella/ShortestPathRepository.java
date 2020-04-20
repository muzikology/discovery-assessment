package za.co.discovery.assignment.interstella;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import za.co.discovery.assignment.interstella.entity.Vertex;
import za.co.discovery.assignment.interstella.helper.Graph;
import za.co.discovery.assignment.interstella.service.PlanetService;
import za.co.discovery.assignment.interstella.service.ShortestPathService;

import javax.annotation.PostConstruct;
import java.util.LinkedList;


@Component
public class ShortestPathRepository {

    private static final String PATH_NOT_AVAILABLE = "There is no path to ";
    private static final String PATH_NOT_NEEDED = "Not needed. You are already on planet ";
    private static final String NO_PLANET_FOUND = "No planet found.";
    private static final String PLANET_DOES_NOT_EXIST = " does not exist in the Interstellar Transport System.";
    protected PlatformTransactionManager platformTransactionManager;
    private Graph graph;

    @Autowired
    private PlanetService planetService;

    @Autowired
    public ShortestPathRepository(@Qualifier("transactionManager") PlatformTransactionManager platformTransactionManager, PlanetService planetService1) {
        this.platformTransactionManager = platformTransactionManager;
        this.planetService = planetService1;
    }

    @PostConstruct
    public void initData() {

        TransactionTemplate tmpl = new TransactionTemplate(platformTransactionManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                planetService.persistGraph();
            }
        });
    }

    public String getShortestPath(String name) {
        StringBuilder path = new StringBuilder();
        graph = planetService.selectGraph();
        ShortestPathService sp = new ShortestPathService(graph);

        if (graph == null || graph.getVertexes() == null || graph.getVertexes().isEmpty()) {
            return NO_PLANET_FOUND;
        }
        Vertex source = graph.getVertexes().get(0);
        Vertex destination = planetService.getVertexByName(name);
        if (destination == null) {
            destination = planetService.getPlanetById(name);
            if (destination == null) {
                return name + PLANET_DOES_NOT_EXIST;
            }
        } else if (source != null && destination != null && source.getVertexId().equals(destination.getVertexId())) {
            return PATH_NOT_NEEDED + source.getName() + ".";
        }

        sp.run(source);
        LinkedList<Vertex> paths = sp.getPath(destination);
        if (paths != null) {
            for (Vertex v : paths) {
                path.append(v.getName() + " (" + v.getVertexId() + ")");
                path.append("\t");
            }
        } else {
            path.append(PATH_NOT_AVAILABLE + destination.getName());
            path.append(".");
        }

        return path.toString();
    }
}
