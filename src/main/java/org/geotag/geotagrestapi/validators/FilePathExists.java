package org.geotag.geotagrestapi.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
public @interface FilePathExists {
    String message() default "File path does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
