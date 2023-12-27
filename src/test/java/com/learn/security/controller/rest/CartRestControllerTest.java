package com.learn.security.controller.rest;

import com.learn.security.service.ItemService;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ItemService itemService;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // CONFIGURE USING REAL APPLICATION SPRING SECURITY CONFIGURATION

        mockMvc = MockMvcBuilders //
                .webAppContextSetup(webApplicationContext) //
                .addFilters(springSecurityFilterChain) //
                .build();
    }

    @Test
    void getCarts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart") //
                        .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart/100") //
                        .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
