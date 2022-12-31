package pt.meetlisbon.backend.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthMsg {
    private String token;
}
