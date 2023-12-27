package com.learn.security.controller.rest;

import com.learn.security.dto.CartItemDTO;
import com.learn.security.dto.ItemDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartRestController {

    @GetMapping
    public List<CartItemDTO> getCarts() {
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    public CartItemDTO getCart(@PathVariable("id") Long id) {
        ItemDTO item = new ItemDTO();
        item.setId(id);
        item.setName("Test");
        item.setDescription("This is a test");
        item.setPrice(100.0);
        return new CartItemDTO(item, 2);
    }

}
