package pt.meetlisbon.backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.meetlisbon.backend.models.entities.Route;
import pt.meetlisbon.backend.models.repository.RouteRepository;

@RestController
@RequestMapping("/api/routes")
@AllArgsConstructor
public class RouteController {
    final RouteRepository routeRepository;

    @GetMapping("")
    public Iterable<Route> getAll() {
        return routeRepository.findAll();
    }
}