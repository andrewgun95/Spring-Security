package com.learn.security.controller.rest;

import com.learn.security.dto.ItemDTO;
import com.learn.security.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
public class ItemRestController {

    private final ItemService itemService;

    public ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @ResponseBody
    public List<ItemDTO> getItems() {
        return itemService.getAll().stream().map(ItemDTO::new).toList();
    }

    @GetMapping("{id}")
    @ResponseBody
    public ItemDTO getItem(@PathVariable("id") Long id) {
        return new ItemDTO(itemService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addItem(@RequestBody ItemDTO itemDTO) {
        itemService.add(itemDTO);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
    }

}
