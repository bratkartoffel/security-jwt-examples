/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_multiple;

import eu.fraho.spring.securityJwt.config.JwtRefreshConfiguration;
import eu.fraho.spring.securityJwt.service.RefreshTokenStore;
import eu.fraho.spring.securityJwt.service.internal.InternalTokenStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class MultiStartersApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(MultiStartersApplication.class, args);
    }

    // we want explicitly use the in memory implementation
    // if not specified this way, the dependency listed latest in build.gradle will be used
    @Bean
    public RefreshTokenStore refreshTokenStore(final JwtRefreshConfiguration jwtRefreshConfiguration,
                                               final UserDetailsService userDetailsService) {
        return new InternalTokenStore(jwtRefreshConfiguration, userDetailsService);
    }
}
