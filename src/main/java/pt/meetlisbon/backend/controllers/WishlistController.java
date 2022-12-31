package pt.meetlisbon.backend.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.meetlisbon.backend.exceptions.NotFoundException;
import pt.meetlisbon.backend.models.entities.Place;
import pt.meetlisbon.backend.models.entities.User;
import pt.meetlisbon.backend.models.entities.Wishlist;
import pt.meetlisbon.backend.models.repository.PlaceRepository;
import pt.meetlisbon.backend.models.repository.UserRepository;
import pt.meetlisbon.backend.models.repository.WishlistRepository;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@AllArgsConstructor
public class WishlistController {
    final PlaceRepository placeRepository;
    final UserRepository userRepository;
    final WishlistRepository wishlistRepository;
    private static final Logger LOG = LoggerFactory.getLogger(WishlistController.class);
    @GetMapping("")
    public Iterable<Place> findByWishlist(Authentication authentication) {
        String usrName = authentication.getName();
        LOG.info("Searching for wishlist for user: {}", usrName);
        return placeRepository.findPlacesByWishlistUser(usrName);
    }

    @Transactional
    @PostMapping("/{placeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addToWishlist(Authentication authentication, @PathVariable UUID placeId) {
        Place place = placeRepository.findPlaceById(placeId);
        User user = userRepository.findUserByUsrNameEquals(authentication.getName());
        if(place == null) throw new NotFoundException("Place");
        else if(user == null) throw new NotFoundException("User");
        Wishlist wishlistEntry = new Wishlist();
        wishlistEntry.setId(UUID.randomUUID());
        wishlistEntry.setPlaceId(place.getId());
        wishlistEntry.setUsersId(user.getId());
        wishlistEntry.setCreatedAt(OffsetDateTime.now());
        wishlistEntry.setUpdatedAt(OffsetDateTime.now());
        wishlistRepository.save(wishlistEntry);
    }

    @Transactional
    @DeleteMapping("/{placeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remFromWishlist(Authentication authentication, @PathVariable UUID placeId) {
        Place place = placeRepository.findPlaceById(placeId);
        User user = userRepository.findUserByUsrNameEquals(authentication.getName());
        if(place == null) throw new NotFoundException("Place");
        else if(user == null) throw new NotFoundException("User");
        Wishlist wishlistEntry = wishlistRepository.findWishlistByUserPlace(user.getId(), placeId);
        if(wishlistEntry == null) throw new NotFoundException("Wishlist");
        wishlistRepository.delete(wishlistEntry);
    }
}