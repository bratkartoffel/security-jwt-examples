/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.test.starter_multiple;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fraho.spring.example.starter_multiple.MultiStartersApplication;
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

import java.util.Map;

@SpringBootTest(classes = MultiStartersApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestMultiStartersApplication {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

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

    private String obtainToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", "foo", "foo"))
                .accept(MediaType.APPLICATION_JSON);

        byte[] body = mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        Map<String, Map<String, String>> obj = objectMapper.readValue(body, new TypeReference<Map<String, Map<String, String>>>() {
        });
        return obj.get("accessToken").get("token");
    }
}
