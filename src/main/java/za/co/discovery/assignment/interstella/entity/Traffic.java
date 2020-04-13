package za.co.discovery.assignment.interstella.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity(name = "traffic")
@Table
public class Traffic implements Serializable {

    @Id
    @Column
    private String routeId;
    @Column
    private String source;
    @Column
    private String destination;
    @Column
    private float delay;

    public Traffic() {
    }

    public Traffic(String routeId, String source, String destination, float delay) {
        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.delay = delay;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }
}

