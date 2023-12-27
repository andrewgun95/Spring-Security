package com.learn.security.entity;

import java.util.Random;

public class Item {

    private Long id;
    private String name;
    private String description;
    private Double price;

    public Item(String name, String description, Double price) {
        this.id = new Random().nextLong(999999);
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
