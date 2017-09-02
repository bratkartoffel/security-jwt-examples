/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example;

import eu.fraho.spring.securityJwt.JwtAuthenticationEntryPoint;
import eu.fraho.spring.securityJwt.config.WebSecurityConfig;
import eu.fraho.spring.securityJwt.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.*;
import java.io.IOException;

@Configuration
@Slf4j
@Order(101)
public class CustomSecurityConfig extends WebSecurityConfig {
    public CustomSecurityConfig(final JwtAuthenticationEntryPoint unauthorizedHandler,
                                final UserDetailsService userDetailsService,
                                final PasswordEncoder passwordEncoder,
                                final JwtTokenService jwtTokenUtil) {
        super(unauthorizedHandler, userDetailsService, passwordEncoder, jwtTokenUtil);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);

        log.info("Overriding ant matchers");
        httpSecurity
                .addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Bean
    public Filter customFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                log.info("Initializing");
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                log.info("Handling request");
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {
                log.info("Destroying");
            }
        };
    }
}