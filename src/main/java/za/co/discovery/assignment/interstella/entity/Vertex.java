package za.co.discovery.assignment.interstella.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity(name = "vertex")
@Table
public class Vertex implements Serializable {

    @Id
    @Column
    private String vertexId;
    @Column
    private String name;

    public Vertex() {
    }

    public Vertex(String vertexId, String name) {
        this.vertexId = vertexId;
        this.name = name;
    }

    public String getVertexId() {
        return vertexId;
    }

    public void setVertexId(String vertexId) {
        this.vertexId = vertexId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vertexId == null) ? 0 : vertexId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (vertexId == null) {
            if (other.vertexId != null)
                return false;
        } else if (!vertexId.equals(other.vertexId))
            return false;
        return true;
    }
}