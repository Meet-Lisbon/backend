package pt.meetlisbon.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pt.meetlisbon.backend.controllers.AuthController;
import pt.meetlisbon.backend.models.entities.Place;
import pt.meetlisbon.backend.models.entities.User;
import pt.meetlisbon.backend.models.repository.PlaceRepository;
import pt.meetlisbon.backend.models.requests.PatchUser;
import pt.meetlisbon.backend.models.requests.RequestPlace;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    public void partialUpdate(RequestPlace requestPlace, Place oldPlace) {
        LOG.info("Updating place, these should match: {} and {}", requestPlace.getPlaceName(), oldPlace.getPlaceName());
        oldPlace.setPlaceName((requestPlace.getPlaceName() == null) ? oldPlace.getPlaceName() : requestPlace.getPlaceName());
        oldPlace.setPlaceLatitude((requestPlace.getPlaceLatitude() == null) ? oldPlace.getPlaceLatitude() : requestPlace.getPlaceLatitude());
        oldPlace.setPlaceLongitude((requestPlace.getPlaceLongitude() == null) ? oldPlace.getPlaceLongitude() : requestPlace.getPlaceLongitude());
        oldPlace.setPlaceAddress((requestPlace.getPlaceAddress() == null) ? oldPlace.getPlaceAddress() : requestPlace.getPlaceAddress());
        oldPlace.setPlaceDescription((requestPlace.getPlaceDescription() == null) ? oldPlace.getPlaceDescription() : requestPlace.getPlaceDescription());
        oldPlace.setUpdatedAt(OffsetDateTime.now());

        placeRepository.save(oldPlace);
    }

}