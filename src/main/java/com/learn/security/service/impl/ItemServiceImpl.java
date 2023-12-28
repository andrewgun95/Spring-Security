package com.learn.security.service.impl;

import com.learn.security.dto.ItemDTO;
import com.learn.security.entity.Item;
import com.learn.security.repository.ItemRepository;
import com.learn.security.service.ItemService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PostConstruct
    public void init() {
        add(new ItemDTO("Shoe", "This is my shoes", 200.0));
        add(new ItemDTO("Computer", "This is my computer", 1000.0));
        add(new ItemDTO("Car", "This is my car", 2000.0));
    }

    @Override
    public void add(ItemDTO item) {
        Item entity = new Item(item.getName(), item.getDescription(), item.getPrice());
        itemRepository.save(entity);
    }

    @Override
    public Item get(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        return itemRepository.findById(id).map(item -> {
            itemRepository.delete(item);
            return true;
        }).orElse(false);
    }

    @Override
    public Collection<Item> getAll() {
        return itemRepository.findAll();
    }
}
