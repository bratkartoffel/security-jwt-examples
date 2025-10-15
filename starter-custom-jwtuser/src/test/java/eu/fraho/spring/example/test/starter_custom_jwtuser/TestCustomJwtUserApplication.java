/*
 * MIT Licence
 * Copyright (c) 2025 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.test.starter_custom_jwtuser;

import eu.fraho.spring.example.starter_custom_jwtuser.CustomJwtUserApplication;
import eu.fraho.spring.example.starter_custom_jwtuser.MyJwtUser;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import eu.fraho.spring.securityJwt.base.service.LoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = CustomJwtUserApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestCustomJwtUserApplication {
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LoginService loginService;

    @Test
    public void testPrivateNoToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/private");
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testPrivateGarbageToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/private").header("Authorization", "Bearer foobar");
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testPrivateWithToken() throws Exception {
        String token = obtainToken();
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/private").header("Authorization", token);
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicNoToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/public");
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicGarbageToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/public").header("Authorization", "Bearer foobar");
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicWithToken() throws Exception {
        String token = obtainToken();
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/public").header("Authorization", token);
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRefreshEnabled() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", "foo", "foo"))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken.token").exists());
    }

    @Test
    public void testCustomJwtUser() throws Exception {
        String token = obtainToken();

        Optional<JwtUser> jwtUser = jwtTokenService.parseUser(token);
        Assertions.assertTrue(jwtUser.isPresent(), "JwtUser should be parsed");
        Assertions.assertEquals(MyJwtUser.class, jwtUser.get().getClass(), "JwtUser hsould be custom instance");
    }

    @Test
    public void testCustomJwtUserNewInstance() throws Exception {
        String token = obtainToken();

        Optional<JwtUser> jwtUser1 = jwtTokenService.parseUser(token);
        Optional<JwtUser> jwtUser2 = jwtTokenService.parseUser(token);

        Assertions.assertTrue(jwtUser1.isPresent(), "JwtUser1 should be parsed");
        Assertions.assertTrue(jwtUser2.isPresent(), "JwtUser2 should be parsed");

        Assertions.assertEquals(MyJwtUser.class, jwtUser1.get().getClass(), "JwtUser should be custom class");
        Assertions.assertEquals(UUID.fromString("550e8400-e29b-11d4-a716-446655440000"), ((MyJwtUser) jwtUser1.get()).getUuid(), "JwtUser should should have custom proprty set");
        Assertions.assertNotSame(jwtUser1.get(), jwtUser2.get(), "JwtUsers should be not the same instance");
    }

    private String obtainToken() {
        return obtainToken("foo");
    }

    private String obtainToken(String username) {
        return loginService.checkLogin(AuthenticationRequest.builder()
                        .username(username).password(username).build())
                .getAccessToken().getToken();
    }
}
