package pt.meetlisbon.backend.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LoginUser {
    private String usrName;
    private String usrPassword;
}
