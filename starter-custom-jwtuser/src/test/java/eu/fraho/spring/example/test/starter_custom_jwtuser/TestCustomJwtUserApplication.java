/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.test.starter_custom_jwtuser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fraho.spring.example.starter_custom_jwtuser.CustomJwtUserApplication;
import eu.fraho.spring.example.starter_custom_jwtuser.MyJwtUser;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import org.junit.Assert;
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
import java.util.Optional;

@SpringBootTest(classes = CustomJwtUserApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestCustomJwtUserApplication {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private JwtTokenService jwtTokenService;
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
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", "foo", "foo"))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken.token").exists());
    }

    @Test
    public void testCustomJwtUser() throws Exception {
        String token = obtainToken();

        Optional<JwtUser> jwtUser = jwtTokenService.parseUser(token);
        Assert.assertTrue("JwtUser should be parsed", jwtUser.isPresent());
        Assert.assertEquals("JwtUser hsould be custom instance", MyJwtUser.class, jwtUser.get().getClass());
    }

    @Test
    public void testCustomJwtUserNewInstance() throws Exception {
        String token = obtainToken();

        Optional<JwtUser> jwtUser1 = jwtTokenService.parseUser(token);
        Optional<JwtUser> jwtUser2 = jwtTokenService.parseUser(token);

        Assert.assertTrue("JwtUser1 should be parsed", jwtUser1.isPresent());
        Assert.assertTrue("JwtUser2 should be parsed", jwtUser2.isPresent());

        Assert.assertEquals("JwtUser should be custom class", MyJwtUser.class, jwtUser1.get().getClass());
        Assert.assertEquals("JwtUser should should have custom proprty set", "this is an example", ((MyJwtUser) jwtUser1.get()).getFoobar());
        Assert.assertNotSame("JwtUsers should be not the same instance", jwtUser1.get(), jwtUser2.get());
    }

    private String obtainToken() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/auth/login")
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

    public WebApplicationContext getWebApplicationContext() {
        return this.webApplicationContext;
    }
}
