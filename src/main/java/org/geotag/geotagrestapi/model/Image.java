package org.geotag.geotagrestapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.geotag.geotagrestapi.utils.json.JsonToPointDeserializer;
import org.geotag.geotagrestapi.utils.json.PointToJsonSerializer;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "images")
@Data
@FieldNameConstants
public class Image {
    private static final String B64_REGEXP = "[a-zA-Z0-9+/=]*";

    public Image() {
    }

    public Image(final String title, final String description, final String filename, final String encodedFilename,
                 final Point location, final String deviceId, final String base64Content) {
        this.title = title;
        this.description = description;
        this.filename = filename;
        this.encodedFilename = encodedFilename;
        this.location = location;
        this.deviceId = deviceId;
        this.base64Content = base64Content;
    }

    public Image(final String title, final String description, final String filename, final String encodedFilename,
                 final Point location, final String deviceId) {
        this(title, description, filename, encodedFilename, location, deviceId, null);
    }

    public Image(final String title, final String description, final String filename, final Point location,
                 final String deviceId) {
        this(title, description, filename, null, location, deviceId, null);
    }

    public Image(final String title, final String description, final String filename, final Point location,
                 final String deviceId, final String base64Content) {
        this(title, description, filename, null, location, deviceId, base64Content);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    @Size(max = 255, message = "Title is to big")
    private String title;

    @Column (length = 1024)
    @Size(max = 1024, message = "Description is too big")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Filename cannot be null")
    @Size(max = 255)
    private String filename;

    @Column(name = "encoded_filename", nullable = false)
    @Size(max = 255)
    private String encodedFilename;

    @Column(nullable = false)
    @NotNull(message = "Location cannot be null")
    @JsonSerialize(using = PointToJsonSerializer.class)
    @JsonDeserialize(using = JsonToPointDeserializer.class)
    private Point location;

    @Column(name = "device_id", nullable = false)
    @NotNull(message = "Device ID cannot be null")
    @Size(max = 255)
    private String deviceId;

    @Transient
    @NotEmpty(message = "Image must have content")
    @Pattern(regexp = Image.B64_REGEXP, message = "Content must be in B64 format")
    private String base64Content;
}
