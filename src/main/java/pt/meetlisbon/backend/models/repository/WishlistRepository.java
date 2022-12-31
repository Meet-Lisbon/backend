package pt.meetlisbon.backend.models.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import pt.meetlisbon.backend.models.entities.Wishlist;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, UUID> {
    @Query(
            value = "SELECT * from users, places, wishlist " +
                    "where usr_id=users_id and " +
                    "place_id = places_id and " +
                    "usr_id = :userId and " +
                    "place_id = :placeId",
            nativeQuery = true
    )
    Wishlist findWishlistByUserPlace(@Param("userId") UUID userId, @Param("placeId") UUID placeId);
}
