/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.test.starter_cookies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fraho.spring.example.starter_cookies.CookiesApplication;
import eu.fraho.spring.securityJwt.base.config.RefreshProperties;
import eu.fraho.spring.securityJwt.base.config.TokenProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(classes = CookiesApplication.class)
@ExtendWith(SpringExtension.class)
public class TestCookiesApplication {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private TokenProperties tokenConfiguration;
    @Autowired
    private RefreshProperties refreshConfiguration;
    private MockMvc mockMvc;

    @BeforeEach
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
    public void testPrivateGarbageTokenHeader() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/private")
                .header("Authorization", "Bearer foobar");
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testPrivateGarbageTokenCookie() throws Exception {
        Cookie cookie = new Cookie(tokenConfiguration.getCookie().getNames()[0], "foobar");
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/private")
                .cookie(cookie);
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testPrivateWithTokenHeader() throws Exception {
        String token = obtainToken();
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/private")
                .header("Authorization", token);
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testPrivateWithTokenCookie() throws Exception {
        Map<String, Cookie> cookies = obtainCookies();
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/private")
                .cookie(cookies.get(tokenConfiguration.getCookie().getNames()[0]));
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicNoToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/public");
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicGarbageTokenHeader() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/public")
                .header("Authorization", "Bearer foobar");
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicGarbageTokenCookie() throws Exception {
        Cookie cookie = new Cookie(tokenConfiguration.getCookie().getNames()[0], "foobar");
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/public")
                .cookie(cookie);
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicWithTokenHeader() throws Exception {
        String token = obtainToken();
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/public")
                .header("Authorization", token);
        mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicWithTokenCookie() throws Exception {
        Map<String, Cookie> cookies = obtainCookies();
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/public")
                .cookie(cookies.get(tokenConfiguration.getCookie().getNames()[0]));
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
    public void testRefreshCookieFlow() throws Exception {
        Map<String, Cookie> cookies = obtainCookies();
        Cookie accessToken = cookies.get(tokenConfiguration.getCookie().getNames()[0]);
        accessToken.setValue("foobar");
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/private")
                .cookie(accessToken);

        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(""));

        req = MockMvcRequestBuilders.post("/auth/refresh")
                .cookie(cookies.get(refreshConfiguration.getCookie().getNames()[0]));

        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(tokenConfiguration.getCookie().getNames()[0]))
                .andExpect(MockMvcResultMatchers.cookie().exists(refreshConfiguration.getCookie().getNames()[0]));
    }

    private String obtainToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", "foo", "foo"))
                .accept(MediaType.APPLICATION_JSON);

        byte[] body = mockMvc.perform(req)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        Object obj = objectMapper.readValue(body, new TypeReference<Map<String, Map<String, String>>>() {
        });
        Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) obj;
        return result.get("accessToken").get("token");
    }

    /**
     * @return name -> cookie
     * @throws Exception If the request fails
     */
    private Map<String, Cookie> obtainCookies() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\",\"password\":\"user\"}")
                .accept(MediaType.APPLICATION_JSON);

        return Arrays.stream(mockMvc.perform(req)
                .andReturn()
                .getResponse()
                .getCookies()).collect(Collectors.toMap(Cookie::getName, c -> c));
    }
}
