/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.regular_no_refresh;

import eu.fraho.spring.securityJwt.dto.JwtUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsService implements UserDetailsService {
    @NonNull
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUser user = new JwtUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(username));
        user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setApiAccessAllowed(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        return user;
    }
}
