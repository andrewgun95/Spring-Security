package com.learn.security.controller;

import com.learn.security.entity.Item;
import com.learn.security.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public String item(@PathVariable("id") Long id, Model model) {
        Item item = itemService.get(id);
        model.addAttribute("name", item.getName());
        model.addAttribute("description", item.getDescription());
        model.addAttribute("price", item.getPrice());
        return "detail";
    }

    @GetMapping("/my/add")
    public String addItem() {
        return "add";
    }
}
