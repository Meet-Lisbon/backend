package pt.meetlisbon.backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InvalidCodeException extends RuntimeException {
    String message;
}