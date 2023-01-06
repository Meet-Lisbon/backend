package pt.meetlisbon.backend.models.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
//    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "cat_id", nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "cat_name", nullable = false)
    private String catName;

    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "cat_icon", nullable = false)
    private String catIcon;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}