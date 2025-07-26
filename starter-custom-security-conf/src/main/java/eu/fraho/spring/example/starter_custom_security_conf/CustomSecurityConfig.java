/*
 * MIT Licence
 * Copyright (c) 2025 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_custom_security_conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Configuration
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(CustomSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity httpSecurity) {
        log.info("Adding custom filter");
        httpSecurity.addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public Filter customFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) {
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