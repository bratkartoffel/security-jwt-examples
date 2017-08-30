/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.Security;

@SpringBootApplication(scanBasePackages = {"eu.fraho.spring.example", "eu.fraho.spring.securityJwt"})
@Slf4j
public class RegularNoRefreshApplication {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(RegularNoRefreshApplication.class, args);
    }
}
