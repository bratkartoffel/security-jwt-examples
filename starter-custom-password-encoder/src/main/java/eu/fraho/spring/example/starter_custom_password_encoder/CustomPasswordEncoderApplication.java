/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_custom_password_encoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CustomPasswordEncoderApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(CustomPasswordEncoderApplication.class, args);
    }
}
