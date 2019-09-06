package org.geotag.geotagrestapi.validators.impl;

import org.geotag.geotagrestapi.validators.DirectoryPath;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathPointsToDirectoryValidator implements ConstraintValidator<DirectoryPath, String> {
    @Override
    public boolean isValid(String path, ConstraintValidatorContext constraintValidatorContext) {
        Path potentialDirectoryPath = Paths.get(path);

        return Files.isDirectory(potentialDirectoryPath);
    }

}
