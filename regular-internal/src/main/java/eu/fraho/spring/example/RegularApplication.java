/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example;

import eu.fraho.spring.securityJwt.config.CryptConfiguration;
import eu.fraho.spring.securityJwt.password.CryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"eu.fraho.spring.example", "eu.fraho.spring.securityJwt"})
@Slf4j
public class RegularApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(RegularApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(final CryptConfiguration configuration) {
        return new CryptPasswordEncoder(configuration);
    }
}
