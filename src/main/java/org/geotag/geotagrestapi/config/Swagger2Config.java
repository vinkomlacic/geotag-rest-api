package org.geotag.geotagrestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("org.geotag.geotagrestapi.controller"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo());
    }


    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Geotag REST API")
                .description("REST API for accessing images stored on the server")
                .contact(new Contact("Vinko Mlačić", "", "vinkomlacic@outlook.com"))
                .license("MIT")
                .licenseUrl("https://opensource.org/licenses/MIT")
                .version("1.0.0")
                .build();
    }
}
