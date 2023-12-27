package com.learn.security.dto;

import com.learn.security.entity.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;

    public ItemDTO(Item entity) {
        this.id = entity != null ? entity.getId() : -1L;
        this.name = entity != null ? entity.getName() : "";
        this.description = entity != null ? entity.getDescription() : "";
        this.price = entity != null ? entity.getPrice() : 0.0;
    }

    public ItemDTO(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
