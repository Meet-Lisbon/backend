package pt.meetlisbon.backend.models.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.meetlisbon.backend.models.entities.Place;

import java.util.List;
import java.util.UUID;


@Repository
public interface PlaceRepository extends CrudRepository<Place, UUID> {
    Place findPlaceById(UUID id);
    Place findPlaceByPlaceName(String placeName);
    @Query(
            value = "SELECT * from places, categories, places_categories " +
                    "where cat_id=categories_id and " +
                    "place_id = places_id and " +
                    "cat_name = :catName",
            nativeQuery = true
    )
    Iterable<Place> findPlacesByCategoryName(@Param("catName") String catName);

    @Query(
            value = "SELECT * from users, places, wishlist " +
                    "where usr_id=users_id and " +
                    "place_id = places_id and " +
                    "usr_name = :userName",
            nativeQuery = true
    )
    Iterable<Place> findPlacesByWishlistUser(@Param("userName") String userName);

    @Query(
            value = "SELECT p.* " +
                    "FROM routes " +
                    "INNER JOIN routes_places rp on routes.route_id = rp.routes_id " +
                    "INNER JOIN places p on p.place_id = rp.places_id " +
                    "WHERE route_id = :routeId",
            nativeQuery = true
    )
    List<Place> getPlacesByRouteId(@Param("routeId") UUID routeId);

    @Query(
            value = "SELECT p.* " +
                    "FROM routes " +
                    "INNER JOIN routes_places rp on routes.route_id = rp.routes_id " +
                    "INNER JOIN places p on p.place_id = rp.places_id " +
                    "WHERE route_name = :routeName",
            nativeQuery = true
    )
    List<Place> getPlacesByRouteName(@Param("routeName") String routeName);
}
