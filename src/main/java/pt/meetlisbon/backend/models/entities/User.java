package pt.meetlisbon.backend.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
//    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "usr_id", nullable = false)@Type(type="pg-uuid")

    private UUID id;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "usr_first_name", nullable = false)
    private String usrFirstName;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "usr_last_name", nullable = false)
    private String usrLastName;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "usr_name", nullable = false)
    private String usrName;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "usr_email", nullable = false)
    private String usrEmail;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "usr_password_hash", nullable = false)
    @JsonIgnore
    private String usrPasswordHash;

    @NotNull
    @Column(name = "usr_email_verified", nullable = false)
    private Boolean usrEmailVerified = false;

    @NotNull
    @Column(name = "usr_active", nullable = false)
    private Boolean usrActive = false;

    @Column(name = "usr_reset_code")
    private String usrResetCode;

    @Column(name = "usr_register_code")
    private String usrRegisterCode;

    @NotNull
    @Column(name = "usr_reset_code_at", nullable = false)
    private OffsetDateTime usrResetCodeAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "usr_role", columnDefinition = "user_role not null")
    private UserRole usrRole;
}