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
@Table(name = "routes")
public class Route {
    @Id
//    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Type(type="pg-uuid")
    @Column(name = "route_id", nullable = false)
    private UUID id;

    @NotNull
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "route_data", nullable = false)
    private String routeData;

    @NotNull
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "route_name", nullable = false)
    private String routeName;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}