/*
 * MIT Licence
 * Copyright (c) 2025 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.test.regular_internal;

import eu.fraho.spring.example.regular_internal.RegularInternalApplication;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import eu.fraho.spring.securityJwt.base.service.LoginService;
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

@SpringBootTest(classes = RegularInternalApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestRegularApplication {
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
    public void testPrivateWithTokenA() throws Exception {
        String token = obtainToken("userA");
        MockHttpServletRequestBuilder reqA = MockMvcRequestBuilders.post("/user/a").header("Authorization", token);
        mockMvc.perform(reqA).andExpect(MockMvcResultMatchers.status().isOk());
        MockHttpServletRequestBuilder reqB = MockMvcRequestBuilders.post("/user/b").header("Authorization", token);
        mockMvc.perform(reqB).andExpect(MockMvcResultMatchers.status().isForbidden());
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
