package com.learn.security.controller;

import com.learn.security.entity.Item;
import com.learn.security.service.ItemService;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerTest {

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
    public void item() throws Exception {
        Mockito.when(itemService.get(1L)).thenReturn(new Item("Test", "This is a test", 100.0));

        mockMvc.perform(MockMvcRequestBuilders //
                .get("/item/1") //
                .contentType(MediaType.TEXT_HTML) //
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addItem1() throws Exception {
        Mockito.when(itemService.get(1L)).thenReturn(new Item("Test", "This is a test", 100.0));

        mockMvc.perform(MockMvcRequestBuilders //
                .get("/item/my/add") //
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("root", "andregokil"))
                .contentType(MediaType.TEXT_HTML) //
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
