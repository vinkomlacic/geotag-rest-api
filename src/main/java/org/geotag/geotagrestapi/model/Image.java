package org.geotag.geotagrestapi.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "images")
@Data
@FieldNameConstants
public class Image {
    private static final String B64_REGEXP = "[a-zA-Z0-9+/=]*";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    @Size(max = 255, message = "Title is to big")
    private final String title;

    @Column (length = 1024)
    @Size(max = 1024, message = "Description is too big")
    private final String description;

    @Column(nullable = false)
    @NotNull(message = "Filename cannot be null")
    @Size(max = 255)
    private final String filename;

    @Column(nullable = false)
    @NotNull(message = "Location cannot be null")
    private final Point location;

    @Column
    @Size(max = 255)
    private final String deviceId;

    @Transient
    @NotEmpty(message = "Image must have content")
    @Pattern(regexp = Image.B64_REGEXP, message = "Content must be in B64 format")
    private final String base64Content;
}
