package org.geotag.geotagrestapi.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.geo.Point;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    private final String title;

    @Column (length = 1024)
    private final String description;

    @Column(nullable = false)
    private final String filename;

    @Column(nullable = false)
    private final Point location;

    @Column(nullable = false, unique = true)
    private final String deviceId;
}
