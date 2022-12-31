package pt.meetlisbon.backend.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Setter
@Getter
public class CreateUser {
    private String firstName;
    private String lastName;
    private String usrName;
    private String usrEmail;
    private String usrPassword;

}
