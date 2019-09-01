package org.geotag.geotagrestapi.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ImageTest {
    private static Path defaultImagePath;
    private static String defaultImageTitle;
    private static String defaultImageDescription;
    private static String defaultImageEncodedFilename;
    private static Point defaultImageLocation;
    private static String defaultImageDeviceId;
    private static String defaultImageBase64Content;
    private static Validator validator;

    private Image image;

    @BeforeClass
    public static void buildValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeClass
    public static void loadDummyImageProperties() throws IOException {
        defaultImagePath = Paths.get("src/test/resources/dummyDirectory/Arnold_River.jpg");
        byte[] defaultImageBytes = Files.readAllBytes(defaultImagePath);
        defaultImageTitle = "dummy image";
        defaultImageDescription = "dummy image";
        defaultImageEncodedFilename = "encoded filename";
        defaultImageLocation = new GeometryFactory().createPoint(new Coordinate(0, 0));
        defaultImageDeviceId = "#12345";
        defaultImageBase64Content = getBase64StringFrom(defaultImageBytes);
    }

    private static String getBase64StringFrom(final byte[] bytes) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    @Before
    public void buildImage() {
        image = new Image(
                defaultImageTitle,
                defaultImageDescription,
                defaultImagePath.getFileName().toString(),
                defaultImageEncodedFilename,
                defaultImageLocation,
                defaultImageDeviceId,
                defaultImageBase64Content
        );
    }

    @Test
    public void constructor_GivenNullTitle_ShouldBeValid() throws Exception {
        image.setTitle(null);
        assertBeanIsValid(image);
    }

    @Test
    public void constructor_GivenLargeTitle_ShouldProduceAConstraintViolation() throws Exception {
        String threeHundredCharacterLine = createLineOf(300);
        image.setTitle(threeHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullDescription_ShouldBeValid() throws Exception {
        image.setDescription(null);
        assertBeanIsValid(image);
    }

    @Test
    public void constructor_GivenLargeDescription_ShouldProduceAConstraintViolation() throws Exception {
        String elevenHundredCharacterLine = createLineOf(1100);
        image.setDescription(elevenHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullEncodedFilename_ShouldBeValid() throws Exception {
        image.setEncodedFilename(null);
        assertBeanIsValid(image);
    }

    @Test
    public void constructor_GivenLargeEncodedFilename_ShouldProducerAConstraintViolation() throws Exception {
        String threeHundredCharacterLine = createLineOf(300);
        image.setEncodedFilename(threeHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullFilename_ShouldProduceAConstraintViolation() throws Exception {
        image.setFilename(null);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenTooLargeFilename_ShouldProduceAConstraintViolation() throws Exception {
        String threeHundredCharacterLine = createLineOf(300);
        image.setFilename(threeHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullLocation_ShouldProduceAConstraintViolation() throws Exception {
        image.setLocation(null);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullDeviceId_ShouldProduceAConstraintViolation() throws Exception {
        image.setDeviceId(null);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenLargeDeviceId_ShouldProduceAConstraintViolation() throws Exception {
        String threeHundredCharacterLine = createLineOf(300);
        image.setDeviceId(threeHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullBase64Content_ShouldProduceAConstraintViolation() throws Exception {
        image.setBase64Content(null);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenEmptyBase64Content_ShouldProduceAConstraintViolation() throws Exception {
        image.setBase64Content("");
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNotBase64EncodedString_ShouldProduceAConstraintViolation() throws Exception {
        String notBase64Characters = ".,;-";
        image.setBase64Content(notBase64Characters);
        assertBeanIsInvalid(image);
    }

    private String createLineOf(final int numberOfCharacters) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < numberOfCharacters; i++) {
            stringBuilder.append(".");
        }

        return stringBuilder.toString();
    }

    private void assertBeanIsValid(final Image image) {
        Set<ConstraintViolation<Image>> violations = validator.validate(image);
        assertTrue(violations.isEmpty());
    }

    private void assertBeanIsInvalid(final Image image) {
        Set<ConstraintViolation<Image>> violations = validator.validate(image);
        assertFalse(violations.isEmpty());
    }
}
