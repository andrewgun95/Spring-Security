package com.learn.security.controller;

import com.learn.security.entity.Cart;
import com.learn.security.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(value = {"", "/"})
    public String cart(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<Cart> carts = cartService.getCart(userDetails.getUsername());
        model.addAttribute("user", userDetails.getUsername());
        model.addAttribute("carts", carts);
        return "cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpServletRequest request) {
        String user = request.getParameter("user");
        cartService.clearCart(user);
        return "redirect:/profile";
    }

}
