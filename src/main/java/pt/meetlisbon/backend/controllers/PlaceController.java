package pt.meetlisbon.backend.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pt.meetlisbon.backend.exceptions.NotFoundException;
import pt.meetlisbon.backend.models.entities.Category;
import pt.meetlisbon.backend.models.entities.Place;
import pt.meetlisbon.backend.models.repository.CategoryRepository;
import pt.meetlisbon.backend.models.repository.PlaceRepository;
import pt.meetlisbon.backend.models.requests.RequestPlace;
import pt.meetlisbon.backend.services.PlaceService;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/places")
@AllArgsConstructor
public class PlaceController {
    final PlaceRepository placeRepository;
    final CategoryRepository categoryRepository;
    final PlaceService placeService;
    private static final Logger LOG = LoggerFactory.getLogger(PlaceController.class);

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        placeRepository.deleteAll();
    }

    @GetMapping
    public Iterable<Place> findByName(@RequestParam(required = false) String placeName,
                                      @RequestParam(required = false) String catName,
                                      @RequestParam(required = false) UUID placeId,
                                      @RequestParam(required = false) UUID catId) {

        if(placeName != null) {
            LOG.info("Searching for place: {}", placeName);
            Place place = placeRepository.findPlaceByPlaceName(placeName);
            if(place == null) throw new NotFoundException("Place");
            return Collections.singleton(place);
        } else if (placeId != null) {
            LOG.info("Searching for place: {}", placeId);
            Place place = placeRepository.findPlaceById(placeId);
            if(place == null) throw new NotFoundException("Place");
            return Collections.singleton(place);
        } else if (catName != null) {
            LOG.info("Searching for places with catName: {}", catName);
            if (categoryRepository.findCategoryByCatNameEquals(catName) == null) throw new NotFoundException("Category");
            return placeRepository.findPlacesByCategoryName(catName);
        } else if (catId != null) {
            LOG.info("Searching for places with catName: {}", catId);
            Category category = categoryRepository.findCategoryByIdEquals(catId);
            if (category == null) throw new NotFoundException("Category");
            return placeRepository.findPlacesByCategoryName(category.getCatName());
        }
        return placeRepository.findAll();

    }
    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlace(@RequestBody RequestPlace requestPlace) {
        if(placeRepository.findPlaceByPlaceName(requestPlace.getPlaceName()) != null)
            throw new NotFoundException("Place");
        String description;
        if (requestPlace.getPlaceDescription() == null) {
            description = "";
        } else {
            description = requestPlace.getPlaceDescription();
        }
        Place place = new Place(
                UUID.randomUUID(),
                requestPlace.getPlaceName(),
                requestPlace.getPlaceImageUrl(),
                requestPlace.getPlaceLatitude(),
                requestPlace.getPlaceLongitude(),
                requestPlace.getPlaceAddress(),
                description,
                OffsetDateTime.now(),
                OffsetDateTime.now());
        placeRepository.save(place);
    }

    @PatchMapping("/update/{placeName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable String placeName, @RequestBody RequestPlace requestPlace) {
        Place place = placeRepository.findPlaceByPlaceName(placeName);
        if (place == null) {
            throw new NotFoundException("Place");
        }
        placeService.partialUpdate(requestPlace, place);

    }
}