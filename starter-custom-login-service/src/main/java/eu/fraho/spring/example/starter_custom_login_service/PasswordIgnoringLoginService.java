/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_custom_login_service;

import com.nimbusds.jose.JOSEException;
import eu.fraho.spring.securityJwt.dto.AccessToken;
import eu.fraho.spring.securityJwt.dto.AuthenticationRequest;
import eu.fraho.spring.securityJwt.dto.AuthenticationResponse;
import eu.fraho.spring.securityJwt.dto.JwtUser;
import eu.fraho.spring.securityJwt.service.JwtTokenService;
import eu.fraho.spring.securityJwt.service.LoginService;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@NoArgsConstructor
public class PasswordIgnoringLoginService implements LoginService {
    @Setter(onMethod = @__({@Autowired, @NonNull}))
    private JwtTokenService jwtTokenService;

    @Setter(onMethod = @__({@Autowired, @NonNull}))
    private UserDetailsService userDetailsService;

    @Override
    public AuthenticationResponse checkLogin(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        // Perform the basic security
        final Authentication authentication = new TestingAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        final JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final AccessToken accessToken;
        try {
            accessToken = jwtTokenService.generateToken(userDetails);
        } catch (JOSEException e) {
            throw new BadCredentialsException("Token generation failed");
        }

        return AuthenticationResponse.builder().accessToken(accessToken).build();
    }

    @Override
    public boolean isTotpOk(@Nullable Integer totp, @NotNull @NonNull JwtUser userDetails) {
        return true;
    }
}
