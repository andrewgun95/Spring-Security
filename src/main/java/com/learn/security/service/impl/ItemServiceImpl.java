package com.learn.security.service.impl;

import com.learn.security.dto.ItemDTO;
import com.learn.security.entity.Item;
import com.learn.security.service.ItemService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> itemList = new HashMap<>();

    @PostConstruct
    public void init() {
        add(new ItemDTO("Shoe", "This is my shoes", 200.0));
        add(new ItemDTO("Computer", "This is my computer", 1000.0));
        add(new ItemDTO("Car", "This is my car", 2000.0));
    }

    @Override
    public void add(ItemDTO item) {
        itemList.put(item.getId(), new Item(item.getName(), item.getDescription(), item.getPrice()));
    }

    @Override
    public Item get(Long id) {
        return itemList.get(id);
    }

    @Override
    public boolean delete(Long id) {
        if (itemList.get(id) != null) {
            itemList.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Collection<Item> getAll() {
        return itemList.values();
    }
}
