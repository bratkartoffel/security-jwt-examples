/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_multiple;

import eu.fraho.spring.securityJwt.base.config.RefreshProperties;
import eu.fraho.spring.securityJwt.base.service.RefreshTokenStore;
import eu.fraho.spring.securityJwt.internal.service.InternalTokenStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
public class MultiStartersApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultiStartersApplication.class, args);
    }

    // we want explicitly use the in memory implementation
    // if not specified this way, the dependency listed latest in build.gradle will be used
    @Bean
    public RefreshTokenStore refreshTokenStore(RefreshProperties refreshProperties, UserDetailsService userDetailsService) {
        InternalTokenStore tokenStore = new InternalTokenStore();
        tokenStore.setRefreshProperties(refreshProperties);
        tokenStore.setUserDetailsService(userDetailsService);
        return tokenStore;
    }
}
