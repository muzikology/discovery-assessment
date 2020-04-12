package za.co.discovery.assignment.interstella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import za.co.discovery.assignment.interstella.entity.Planet;


public interface PlanetRepository extends JpaRepository<Planet, Long> {

    Planet findByPlanetName(String origin);

}
