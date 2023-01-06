package pt.meetlisbon.backend.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pt.meetlisbon.backend.exceptions.AlreadyExistsException;
import pt.meetlisbon.backend.exceptions.BadRequestException;
import pt.meetlisbon.backend.models.entities.Place;
import pt.meetlisbon.backend.models.entities.Route;
import pt.meetlisbon.backend.models.repository.PlaceRepository;
import pt.meetlisbon.backend.models.repository.RouteRepository;
import pt.meetlisbon.backend.models.requests.RequestRoute;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/routes")
@AllArgsConstructor
public class RouteController {
    final RouteRepository routeRepository;
    final PlaceRepository placeRepository;
    private static final Logger LOG = LoggerFactory.getLogger(RouteController.class);
    @GetMapping()
    public Iterable<Route> getAll() {
        return routeRepository.findAll();
    }

    @PostMapping()
    @ResponseBody
    public Route createRoute(@RequestBody RequestRoute requestRoute) {
        if(routeRepository.existsRouteByRouteName(requestRoute.getRouteName()))
            throw new AlreadyExistsException("Route");
        Route route = new Route();
        route.setId(UUID.randomUUID());
        route.setRouteName(requestRoute.getRouteName());
        route.setRouteData(requestRoute.getRouteData());
        route.setCreatedAt(OffsetDateTime.now());
        route.setUpdatedAt(OffsetDateTime.now());
        routeRepository.save(route);
        LOG.info("Route created");
        return route;
    }

    @PostMapping("/{routeId}/{placeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPlaceToRoute(@PathVariable UUID routeId, @PathVariable UUID placeId) {
        routeRepository.insertPlaceInRoute(routeId, placeId);
    }

    @GetMapping("/places")
    public List<Place> getPlaces(
            @RequestParam(required = false) UUID routeId,
            @RequestParam(required = false) String routeName) {
        if(routeId != null) return placeRepository.getPlacesByRouteId(routeId);
        else if (routeName != null) return placeRepository.getPlacesByRouteName(routeName);
        throw new BadRequestException();
    }
}