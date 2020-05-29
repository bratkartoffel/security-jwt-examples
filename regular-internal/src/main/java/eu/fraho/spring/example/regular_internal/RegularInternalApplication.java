/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.regular_internal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"eu.fraho.spring.example.regular_internal", "eu.fraho.spring.securityJwt"})
public class RegularInternalApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegularInternalApplication.class, args);
    }
}
