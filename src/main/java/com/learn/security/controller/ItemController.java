package com.learn.security.controller;

import com.learn.security.dto.ItemDTO;
import com.learn.security.entity.Item;
import com.learn.security.service.CartService;
import com.learn.security.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final CartService cartService;


    public ItemController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public String item(@PathVariable("id") Long id, Model model) {
        Item item = itemService.get(id);
        model.addAttribute("name", item.getName());
        model.addAttribute("description", item.getDescription());
        model.addAttribute("price", item.getPrice());
        return "detail";
    }

    @GetMapping("/{id}/buy")
    public String buyItem(@PathVariable("id") Long id, @RequestParam(value = "qty", defaultValue = "1") Integer qty) throws Exception {
        cartService.addCartItem(id, qty);
        return "redirect:/cart";
    }

    @GetMapping("/my/add")
    public String newItem(Model model) {
        model.addAttribute("item", new ItemDTO());
        return "add";
    }

    @PostMapping("/my/add")
    public String addItem(@ModelAttribute("item") @Validated ItemDTO itemDTO) {
        itemService.add(itemDTO);
        return "redirect:/profile";
    }

}
