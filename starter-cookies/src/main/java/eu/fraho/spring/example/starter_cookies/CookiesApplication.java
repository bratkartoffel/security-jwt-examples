/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_cookies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class CookiesApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(CookiesApplication.class, args);
    }
}
