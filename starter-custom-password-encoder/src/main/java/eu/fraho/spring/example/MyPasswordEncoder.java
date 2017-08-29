/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyPasswordEncoder implements PasswordEncoder {
    public MyPasswordEncoder() {
        log.info("Using my password encoder");
    }

    @Override
    public String encode(@NonNull CharSequence rawPassword) {
        return rawPassword + "foo";
    }

    @Override
    public boolean matches(@NonNull CharSequence rawPassword, @NonNull String encodedPassword) {
        return (rawPassword + "foo").equals(encodedPassword);
    }
}
