package pt.meetlisbon.backend.models.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class PasswordResetUser {
    private String usrPassword;
}
