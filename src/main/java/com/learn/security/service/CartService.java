package com.learn.security.service;

import com.learn.security.entity.Cart;

import java.util.Collection;

public interface CartService {

    void addCartItem(Long itemId, Integer qty) throws Exception;

    Collection<Cart> getCart(String username);

    void clearCart(String username);

}
