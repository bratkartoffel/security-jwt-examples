/*
 * MIT Licence
 * Copyright (c) 2017 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_custom_security_conf;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.*;
import java.io.IOException;

@Configuration
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        log.info("Adding custom filter");
        httpSecurity.addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class);
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