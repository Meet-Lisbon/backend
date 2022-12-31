package pt.meetlisbon.backend.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "wishlist")
public class Wishlist {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "wishlist_id", nullable = false)
    private UUID id;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "users_id", nullable = false)
    private UUID usersId;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "places_id", nullable = false)
    private UUID placeId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}