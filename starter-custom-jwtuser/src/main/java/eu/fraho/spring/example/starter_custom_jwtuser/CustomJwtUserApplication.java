/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_custom_jwtuser;

import eu.fraho.spring.securityJwt.dto.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@Slf4j
public class CustomJwtUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomJwtUserApplication.class, args);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JwtUser jwtUser() {
        log.debug("Register MyJwtUser");
        return new MyJwtUser();
    }
}
