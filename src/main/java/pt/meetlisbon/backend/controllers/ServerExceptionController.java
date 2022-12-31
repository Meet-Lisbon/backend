package pt.meetlisbon.backend.controllers;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pt.meetlisbon.backend.exceptions.*;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class ServerExceptionController {
    @Bean
    public ErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                return Collections.singletonMap("message", super.getErrorAttributes(webRequest, options)
                        .get("error"));
            }
        };
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ServerException> userNotFoundException(NotFoundException e) {
        ServerException response =
                new ServerException(e.getMessage() + " not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<ServerException> jwtInvalidException() {
        ServerException response =
                new ServerException("Invalid token");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ServerException> invalidCredentials() {
        ServerException response =
                new ServerException("Invalid credentials");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ServerException> userAlreadyExists(AlreadyExistsException e) {
        ServerException response =
                new ServerException(e.getMessage() + " already exists");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ServerException> badRequest() {
        ServerException response =
                new ServerException("Bad request");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<ServerException> notAuthenticated() {
        ServerException response =
                new ServerException("Not authenticated");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ServerException> notAuthorized() {
        ServerException response =
                new ServerException("Invalid permissions");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ServerException> notAuthorized(InvalidCodeException e) {
        ServerException response =
                new ServerException(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}