package com.learn.security.controller;

import com.learn.security.entity.Item;
import com.learn.security.service.CartService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CartService cartService;

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
    public void getAddItemAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders //
                .get("/item/my/add") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("root", "andregokil")).contentType(MediaType.TEXT_HTML) //
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAddItemRoleUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders //
                .get("/item/my/add") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password")).contentType(MediaType.TEXT_HTML) //
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void getAddItemRoleCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders //
                .get("/item/my/add") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "password")).contentType(MediaType.TEXT_HTML) //
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void addItemRoleAdmin() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Test 123");
        params.add("description", "Test 123");
        params.add("price", "123");

        mockMvc.perform(MockMvcRequestBuilders //
                .post("/item/my/add") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("root", "andregokil")) //
                .params(params) //
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) //
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void addItemRoleUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Test 123");
        params.add("description", "Test 123");
        params.add("price", "123");

        mockMvc.perform(MockMvcRequestBuilders //
                .post("/item/my/add") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password")) //
                .params(params) //
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) //
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void addItemRoleCustomer() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Test 123");
        params.add("description", "Test 123");
        params.add("price", "123");

        mockMvc.perform(MockMvcRequestBuilders //
                .post("/item/my/add") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "password")) //
                .params(params) //
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) //
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void buyItemRoleCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders //
                .get("/item/1/buy?qty=1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "password")) //
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    // Admin has a Customer Role
    @Test
    public void buyItemRoleAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders //
                .get("/item/1/buy?qty=1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("root", "andregokil")) //
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void buyItemRoleUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders //
                .get("/item/1/buy?qty=1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password")) //
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
