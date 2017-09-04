/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.regular_internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"eu.fraho.spring.example.regular_internal", "eu.fraho.spring.securityJwt"})
@Slf4j
public class RegularInternalApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(RegularInternalApplication.class, args);
    }
}