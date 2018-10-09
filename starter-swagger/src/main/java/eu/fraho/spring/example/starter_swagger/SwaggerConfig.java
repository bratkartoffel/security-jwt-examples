/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_swagger;

import com.google.common.collect.Lists;
import eu.fraho.spring.securityJwt.base.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    public SwaggerConfig() {
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .protocols(getProtocols())
                .useDefaultResponseMessages(false)
                .genericModelSubstitutes(Optional.class)
                // configure JWT schema
                .securitySchemes(Lists.newArrayList(apiKey()))
                // configure JWT context
                .securityContexts(Lists.newArrayList(securityContext()))
                .select()
                .apis(rh -> RequestHandlerSelectors.basePackage(JwtAuthenticationEntryPoint.class.getPackage().getName()).apply(rh)
                        ||
                        RequestHandlerSelectors.basePackage(SwaggerApplication.class.getPackage().getName()).apply(rh)
                )
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Fraho securityJwt example API",
                "This is just a sample application with swagger2 documentation enabled",
                "0.0.1-SNAPSHOT",
                "https://www.fraho.eu",
                new Contact("Simon Frankenberger",
                        "https://www.fraho.eu",
                        "simon-ossrh-release@fraho.eu"),
                "MIT",
                "http://doge.mit-license.org/",
                Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private List<SecurityReference> defaultAuth() {
        return Lists.newArrayList(new SecurityReference("Authorization", new AuthorizationScope[0]));
    }

    private Set<String> getProtocols() {
        final Set<String> result = new HashSet<>();
        result.add("https");
        result.add("http");
        return result;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.ant("/private/**")).build();
    }
}