package za.co.discovery.assignment.interstella.model;

import java.io.Serializable;

public class ShortestPathModel implements Serializable {

    private String selectedVertex;
    private String selectedVertexName;
    private String vertexId;
    private String vertexName;
    private String thePath;
    private String sourceVertex;
    private String destinationVertex;
    private boolean undirectedGraph;
    private boolean trafficAllowed;

    public String getSelectedVertex() {
        return selectedVertex;
    }

    public void setSelectedVertex(String selectedVertex) {
        this.selectedVertex = selectedVertex;
    }

    public String getVertexId() {
        return vertexId;
    }

    public void setVertexId(String vertexId) {
        this.vertexId = vertexId;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public String getThePath() {
        return thePath;
    }

    public void setThePath(String thePath) {
        this.thePath = thePath;
    }

    public String getSelectedVertexName() {
        return selectedVertexName;
    }

    public void setSelectedVertexName(String selectedVertexName) {
        this.selectedVertexName = selectedVertexName;
    }

    public String getSourceVertex() {
        return sourceVertex;
    }

    public void setSourceVertex(String sourceVertex) {
        this.sourceVertex = sourceVertex;
    }

    public String getDestinationVertex() {
        return destinationVertex;
    }

    public void setDestinationVertex(String destinationVertex) {
        this.destinationVertex = destinationVertex;
    }

    public boolean isUndirectedGraph() {
        return undirectedGraph;
    }

    public void setUndirectedGraph(boolean undirectedGraph) {
        this.undirectedGraph = undirectedGraph;
    }

    public boolean isTrafficAllowed() {
        return trafficAllowed;
    }

    public void setTrafficAllowed(boolean trafficAllowed) {
        this.trafficAllowed = trafficAllowed;
    }
}
