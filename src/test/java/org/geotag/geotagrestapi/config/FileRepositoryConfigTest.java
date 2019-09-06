package org.geotag.geotagrestapi.config;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.*;

import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ImportAutoConfiguration(classes = FileRepositoryConfig.class)
public class FileRepositoryConfigTest {

    private final static Logger logger = LoggerFactory.getLogger(FileRepositoryConfigTest.class);
    private static Validator validator;

    @Autowired
    private FileRepositoryConfig fileRepositoryConfig;

    @BeforeClass
    public static void instantiateValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void getPath_GivenExistingDirectoryPath_ShouldReturnThePath() throws Exception {
        final String EXISTING_DIRECTORY_PATH = "src/test";
        fileRepositoryConfig.setPath(EXISTING_DIRECTORY_PATH);

        assertConfigurationIsValid();
        assertEquals(EXISTING_DIRECTORY_PATH, fileRepositoryConfig.getPath());
    }

    @Test
    public void setPath_GivenNotExistingPath_ShouldThrowPathNotFoundException() throws Exception {
        final String NON_EXISTING_PATH = "E:/not_exists";
        fileRepositoryConfig.setPath(NON_EXISTING_PATH);

        assertConfigurationIsInvalid();
    }

    @Test
    public void setPath_GivenPathToFile_ShouldThrowNotADirectoryException() throws Exception {
        final String NON_DIRECTORY_PATH = "src/test/resources/emptyFile";
        fileRepositoryConfig.setPath(NON_DIRECTORY_PATH);

        assertConfigurationIsInvalid();
    }

    private void assertConfigurationIsValid() {
        assertTrue(configurationIsValid());
    }

    private void assertConfigurationIsInvalid() {
        assertFalse(configurationIsValid());
    }

    private boolean configurationIsValid() {
        Set<ConstraintViolation<FileRepositoryConfig>> violations = validator.validate(fileRepositoryConfig);

        if (violations.isEmpty()) {
            return true;
        } else {
            printViolations(violations);
            return false;
        }
    }

    private void printViolations(final Set<ConstraintViolation<FileRepositoryConfig>> violations) {
        logger.error("Violations: ");
        for (ConstraintViolation<FileRepositoryConfig> violation : violations) {
            logger.error(violation.getMessage());
        }
    }
}
