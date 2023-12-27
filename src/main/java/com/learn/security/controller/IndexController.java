package com.learn.security.controller;

import com.learn.security.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

    private final ItemService itemService;

    public IndexController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value = {"", "/"})
    public String index() {
        return "welcome";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("listItems", itemService.getAll());
        return "profile";
    }

}
