package org.geotag.geotagrestapi.validators;

import org.geotag.geotagrestapi.validators.impl.PathPointsToDirectoryValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {PathPointsToDirectoryValidator.class})
public @interface DirectoryPath {
    String message() default "Path is not a directory";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
