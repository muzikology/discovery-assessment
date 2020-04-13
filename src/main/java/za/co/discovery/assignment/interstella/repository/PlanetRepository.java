package za.co.discovery.assignment.interstella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.discovery.assignment.interstella.entity.Vertex;

import java.util.Optional;


public interface PlanetRepository extends JpaRepository<Vertex, Long> {

    Optional<Vertex> findByName(String origin);
    Vertex getPlanetByVertexId(String id);
    Boolean existsByVertexId(String planetId);

    void deleteByVertexId(Long id);

}
