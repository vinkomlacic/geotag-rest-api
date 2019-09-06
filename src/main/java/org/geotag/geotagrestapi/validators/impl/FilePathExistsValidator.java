package org.geotag.geotagrestapi.validators.impl;

import org.geotag.geotagrestapi.validators.ExistingFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FilePathExistsValidator implements ConstraintValidator<ExistingFile, String> {
    @Override
    public boolean isValid(String filePathString, ConstraintValidatorContext constraintValidatorContext) {
        Path filePath = Paths.get(filePathString);

        return Files.exists(filePath);
    }
}
