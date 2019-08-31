package org.geotag.geotagrestapi.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.geo.Point;

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
        defaultImageLocation = new Point(1, 2);
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
        setBeanProperty(Image.Fields.title, null);
        assertBeanIsValid(image);
    }

    @Test
    public void constructor_GivenLargeTitle_ShouldProduceAConstraintViolation() throws Exception {
        String threeHundredCharacterLine = createLineOf(300);
        setBeanProperty(Image.Fields.title, threeHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullDescription_ShouldBeValid() throws Exception {
        setBeanProperty(Image.Fields.description, null);
        assertBeanIsValid(image);
    }

    @Test
    public void constructor_GivenLargeDescription_ShouldProduceAConstraintViolation() throws Exception {
        String elevenHundredCharacterLine = createLineOf(1100);
        setBeanProperty(Image.Fields.description, elevenHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullEncodedFilename_ShouldBeValid() throws Exception {
        setBeanProperty(Image.Fields.encodedFilename, null);
        assertBeanIsValid(image);
    }

    @Test
    public void constructor_GivenLargeEncodedFilename_ShouldProducerAConstraintViolation() throws Exception {
        String threeHundredCharacterLine = createLineOf(300);
        setBeanProperty(Image.Fields.encodedFilename, threeHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullFilename_ShouldProduceAConstraintViolation() throws Exception {
        setBeanProperty(Image.Fields.filename, null);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenTooLargeFilename_ShouldProduceAConstraintViolation() throws Exception {
        String threeHundredCharacterLine = createLineOf(300);
        setBeanProperty(Image.Fields.filename, threeHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullLocation_ShouldProduceAConstraintViolation() throws Exception {
        setBeanProperty(Image.Fields.location, null);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullDeviceId_ShouldProduceAConstraintViolation() throws Exception {
        setBeanProperty(Image.Fields.deviceId, null);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenLargeDeviceId_ShouldProduceAConstraintViolation() throws Exception {
        String threeHundredCharacterLine = createLineOf(300);
        setBeanProperty(Image.Fields.deviceId, threeHundredCharacterLine);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNullBase64Content_ShouldProduceAConstraintViolation() throws Exception {
        setBeanProperty(Image.Fields.base64Content, null);
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenEmptyBase64Content_ShouldProduceAConstraintViolation() throws Exception {
        setBeanProperty(Image.Fields.base64Content, "");
        assertBeanIsInvalid(image);
    }

    @Test
    public void constructor_GivenNotBase64EncodedString_ShouldProduceAConstraintViolation() throws Exception {
        String notBase64Characters = ".,;-";
        setBeanProperty(Image.Fields.base64Content, notBase64Characters);
        assertBeanIsInvalid(image);
    }

    private void setBeanProperty(final String property, final Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = Image.class.getDeclaredField(property);
        field.setAccessible(true);
        field.set(image, value);
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
        Assert.assertTrue(violations.isEmpty());
    }

    private void assertBeanIsInvalid(final Image image) {
        Set<ConstraintViolation<Image>> violations = validator.validate(image);
        Assert.assertFalse(violations.isEmpty());
    }
}
