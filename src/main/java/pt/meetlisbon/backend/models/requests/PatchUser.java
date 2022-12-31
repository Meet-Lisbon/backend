package pt.meetlisbon.backend.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pt.meetlisbon.backend.models.entities.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class PatchUser {
    private UUID uuid;
    private String usrFirstName;
    private String usrLastName;
    private String usrName;
    private String usrEmail;
    private String usrPasswordHash;
    private Boolean usrEmailVerified;
    private Boolean usrActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UserRole usrRole;
}
