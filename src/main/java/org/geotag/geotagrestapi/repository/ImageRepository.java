package org.geotag.geotagrestapi.repository;

import org.geotag.geotagrestapi.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    Image getImageByDeviceId(final String deviceId);
}
