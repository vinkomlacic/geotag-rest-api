package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.config.FileRepositoryConfig;
import org.geotag.geotagrestapi.utils.UniqueFilenameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

public class StorageAccessBaseTest {

    protected static final String DUMMY_DIRECTORY_PATH = "src/test/resources/dummyDirectory";

    @TestConfiguration
    static class ImageStoreServiceContextConfiguration {

        @Bean
        public ImageStoreService imageStoreService() {
            return new Base64ImageStoreService();
        }

        @Bean
        public ImageRetrievalService imageRetrievalService() {
            return new Base64ImageRetrievalService();
        }

        @Bean
        public UniqueFilenameGenerator uniqueFilenameGenerator() {
            return new UniqueFilenameGenerator();
        }

        @Bean
        public FileRepositoryConfig fileRepositoryConfig() throws Exception {
            FileRepositoryConfig fileRepositoryConfig = new FileRepositoryConfig();
            fileRepositoryConfig.setPath(DUMMY_DIRECTORY_PATH);
            return fileRepositoryConfig;
        }
    }

    @Autowired
    protected ImageStoreService imageStoreService;

    @Autowired
    protected ImageRetrievalService imageRetrievalService;

    @Autowired
    protected FileRepositoryConfig fileRepositoryConfig;
}
