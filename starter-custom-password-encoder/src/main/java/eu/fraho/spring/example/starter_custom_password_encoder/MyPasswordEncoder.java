/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_custom_password_encoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword + "foo";
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return (rawPassword + "foo").equals(encodedPassword);
    }
}
