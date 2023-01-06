package pt.meetlisbon.backend.models.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.meetlisbon.backend.models.entities.Route;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public interface RouteRepository extends CrudRepository<Route, UUID> {
    @Override
    boolean existsById(@NonNull UUID uuid);
    boolean existsRouteByRouteName(String name);


    @Modifying
    @Transactional
    @Query(
            value = "insert into routes_places (routes_id, places_id) values (" +
                    "    (SELECT route_id from routes where route_id = :routeId)," +
                    "    (SELECT place_id from places where place_id = :placeId)" +
                    ");",
            nativeQuery = true
    )
    void insertPlaceInRoute(@Param("routeId") UUID routeId, @Param("placeId") UUID placeId);
}
