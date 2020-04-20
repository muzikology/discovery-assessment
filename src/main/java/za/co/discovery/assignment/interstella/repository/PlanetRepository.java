package za.co.discovery.assignment.interstella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.discovery.assignment.interstella.entity.Vertex;


public interface PlanetRepository extends JpaRepository<Vertex, Long> {

    Vertex findByName(String vertex);
    Vertex getPlanetByVertexId(String id);
    Boolean existsByVertexId(String planetId);

    void deleteByVertexId(String id);

}
