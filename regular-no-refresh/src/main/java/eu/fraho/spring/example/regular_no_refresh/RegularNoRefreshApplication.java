/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.regular_no_refresh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"eu.fraho.spring.example.regular_no_refresh", "eu.fraho.spring.securityJwt"})
public class RegularNoRefreshApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegularNoRefreshApplication.class, args);
    }
}
