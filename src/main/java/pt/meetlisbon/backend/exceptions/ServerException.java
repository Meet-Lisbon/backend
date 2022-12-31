package pt.meetlisbon.backend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
@JsonIgnoreProperties({ "stackTrace", "cause", "suppressed", "localizedMessage"})
public class ServerException extends RuntimeException {
    private final String message;
}