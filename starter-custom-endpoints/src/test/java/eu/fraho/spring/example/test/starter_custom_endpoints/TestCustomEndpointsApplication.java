/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.test.starter_custom_endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fraho.spring.example.starter_custom_endpoints.CustomEndpointsApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.Map;

@SpringBootTest(classes = CustomEndpointsApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestCustomEndpointsApplication {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private Filter springSecurityFilterChain;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
        }
    }

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
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/foobar/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", "foo", "foo"))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken.token").exists());
    }

    @Test
    public void testRefresh() throws Exception {
        String refreshToken = obtainRefreshToken();

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/foobar/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"username\":\"%s\",\"refreshToken\":\"%s\"}", "foo", refreshToken))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken.token").exists());
    }

    private String obtainToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/foobar/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", "foo", "foo"))
                .accept(MediaType.APPLICATION_JSON);

        byte[] body = mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return (objectMapper.readValue(body, new TypeReference<Map<String, Map<String, String>>>() {
        }).get("accessToken")).get("token");
    }

    private String obtainRefreshToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/foobar/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", "foo", "foo"))
                .accept(MediaType.APPLICATION_JSON);

        byte[] body = mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return (objectMapper.readValue(body, new TypeReference<Map<String, Map<String, String>>>() {
        }).get("refreshToken")).get("token");
    }

    public WebApplicationContext getWebApplicationContext() {
        return this.webApplicationContext;
    }
}
