package com.learn.security.controller.rest;

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
public class ItemRestControllerTest {

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

    // WITH NO CREDENTIALS

    @Test
    public void getItems() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/item") //
                        .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/item/100") //
                        .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // USING USERNAME PASSWORD CREDENTIALS

    @Test
    public void addItemAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/item") //
                .contentType(MediaType.APPLICATION_JSON) //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("root", "andregokil")) //
                .content("{\"name\":\"Test\",\"description\":\"\",\"price\":100.0}") //
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addItemUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/item") //
                .contentType(MediaType.APPLICATION_JSON) //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password")) //
                .content("{\"name\":\"Test\",\"description\":\"\",\"price\":100.0}") //
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addItemCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/item") //
                .contentType(MediaType.APPLICATION_JSON) //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "password")) //
                .content("{\"name\":\"Test\",\"description\":\"\",\"price\":100.0}") //
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteItemUsingUsernameAndPassword() throws Exception {
        Mockito.when(itemService.delete(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/item/1") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("root", "andregokil")) //
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteItemUsingUsernameAndPasswordUserRole() throws Exception {
        Mockito.when(itemService.delete(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/item/1") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password")) //
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void deleteItemUsingUsernameAndPasswordCustomerRole() throws Exception {
        Mockito.when(itemService.delete(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/item/1") //
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "password")) //
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    // USING API KEY AND SECRET CREDENTIALS

    @Test
    public void deleteItemUsingAPIKeyAndSecret() throws Exception {
        Mockito.when(itemService.delete(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/item/1") //
                .header("API-Key", "31bKytEXVYJOkndaQk98M7GW9q87nEN2MVc6PMHevcieSZ1gFl6pAWHyxor4ILGB")
                .header("API-Secret", "dyHdaaFFlcNSe4V2QLoXDAExglC71xJnZZjODD0I158NJmfQGqifnVmYBoUengKH")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteItemUsingAPIKeyAndSecretBadCredentials() throws Exception {
        Mockito.when(itemService.delete(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/item/1") //
                .header("API-Key", "31bKytEXVYJOkndaQk98M7GW9q87nEN2MVc6PMHevcieSZ1gFl6pAWHyxor4ILGB")
                .header("API-Secret", "abc")
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    // USING BEARER TOKEN CREDENTIALS

    @Test
    public void deleteItemUsingBearerToken() throws Exception {
        Mockito.when(itemService.delete(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/item/1") //
                .header("Authorization", "Bearer user:password")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteItemUsingTokenBadCredentials() throws Exception {
        Mockito.when(itemService.delete(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/item/1") //
                .header("Authorization", "Bearer asdfhas")
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


}
