package pt.meetlisbon.backend.models.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.meetlisbon.backend.models.entities.Route;

import java.util.UUID;

@Repository
public interface RouteRepository extends CrudRepository<Route, UUID> {
}
