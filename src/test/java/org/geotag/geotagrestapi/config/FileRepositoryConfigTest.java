package org.geotag.geotagrestapi.config;

import org.geotag.geotagrestapi.exceptions.NotADirectoryException;
import org.geotag.geotagrestapi.exceptions.PathNotFoundException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileRepositoryConfigTest {

    private FileRepositoryConfig fileRepositoryConfig = new FileRepositoryConfig();

    @Test
    public void getPath_GivenExistingDirectoryPath_ShouldReturnThePath() throws Exception {
        final String EXISTING_DIRECTORY_PATH = "src";
        fileRepositoryConfig.setPath(EXISTING_DIRECTORY_PATH);
        assertEquals(EXISTING_DIRECTORY_PATH, fileRepositoryConfig.getPath());
    }

    @Test(expected = PathNotFoundException.class)
    public void setPath_GivenNotExistingPath_ShouldThrowPathNotFoundException() throws Exception {
        final String NON_EXISTING_PATH = "E:/not_exists";
        fileRepositoryConfig.setPath(NON_EXISTING_PATH);
    }

    @Test(expected = NotADirectoryException.class)
    public void setPath_GivenPathToFile_ShouldThrowNotADirectoryException() throws Exception {
        final String NON_DIRECTORY_PATH = "src/test/resources/emptyFile";
        fileRepositoryConfig.setPath(NON_DIRECTORY_PATH);
    }
}
