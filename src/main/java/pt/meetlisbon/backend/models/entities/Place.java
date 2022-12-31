package pt.meetlisbon.backend.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "places")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Place {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "place_id", nullable = false)
    private UUID id;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "place_name", nullable = false)
    private String placeName;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "place_image_url", nullable = false)
    private String placeImageUrl;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "place_latitude", nullable = false)
    private String placeLatitude;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "place_longitude", nullable = false)
    private String placeLongitude;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "place_address")
    private String placeAddress;
    
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "place_description")
    private String placeDescription;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}