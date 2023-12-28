package com.learn.security.service.impl;

import com.learn.security.entity.Cart;
import com.learn.security.entity.Item;
import com.learn.security.entity.User;
import com.learn.security.repository.CartRepository;
import com.learn.security.repository.ItemRepository;
import com.learn.security.repository.UserRepository;
import com.learn.security.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
public class CartServiceImpl implements CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    public CartServiceImpl(ItemRepository itemRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void addCartItem(Long itemId, Integer qty) throws Exception {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new Exception("Item is not found."));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        cartRepository.save(new Cart(item, qty, user));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Cart> getCart(String username) {
        return userRepository.findByUsername(username) //
                .map(user -> cartRepository.findByUserId(user.getId())) //
                .orElse(Collections.emptyList());
    }

    @Transactional
    @Override
    public void clearCart(String username) {
        userRepository.findByUsername(username) //
                .ifPresent(user -> cartRepository.deleteByUserId(user.getId())); //
    }
}
