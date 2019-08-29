package org.geotag.geotagrestapi.repository;

import org.geotag.geotagrestapi.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    Set<Image> getImagesByDeviceId(final String deviceId);
}
