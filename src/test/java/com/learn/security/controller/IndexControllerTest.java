package com.learn.security.controller;

import com.learn.security.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
public class IndexControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SecurityFilterChain springSecurityFilterChain;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // CONFIGURE USING DEFAULT/MOCK SPRING SECURITY CONFIGURATION

        mockMvc = MockMvcBuilders //
                .webAppContextSetup(webApplicationContext) //
                .apply(SecurityMockMvcConfigurers.springSecurity()) //
                .build();
    }

    // TEST FOR SECURITY LOGIC

    // To avoiding for set authorization header every requests on Spring Security
    // Create a Mock Login
    @WithMockUser(username = "test") // doesn't need to precise with login credentials
    @Test
    void indexWithMockLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/") //
                        .contentType(MediaType.TEXT_HTML)) //
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // TEST FOR AUTHENTICATION LOGIC

    @Test
    void profileWithHttpBasic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile") //
//                        .header("Authorization", "Basic " + HttpHeaders.encodeBasicAuth("root", "andregokil", Charset.defaultCharset())) //
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("root", "andregokil"))//
                        .contentType(MediaType.TEXT_HTML)) //
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
