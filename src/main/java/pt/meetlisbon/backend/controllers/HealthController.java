package pt.meetlisbon.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.meetlisbon.backend.exceptions.ServerException;
import pt.meetlisbon.backend.models.requests.HealthMsg;

/**
 * This endpoint is responsible for translating
 * the state of the server into a measure of health.
 * <p>
 * Currently, it only returns a {@link HealthMsg}.
 * @see #health()
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * Returns a {@link ServerException} saying "OK".
     */
    @GetMapping
    public HealthMsg health() {
        return new HealthMsg("OK");
    }

}
