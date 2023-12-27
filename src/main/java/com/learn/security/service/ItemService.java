package com.learn.security.service;

import com.learn.security.dto.ItemDTO;
import com.learn.security.entity.Item;

import java.util.Collection;

public interface ItemService {

    void add(ItemDTO item);

    Item get(Long id);

    Collection<Item> getAll();

    boolean delete(Long id);

}
