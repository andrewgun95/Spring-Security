package com.learn.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDTO {

    private ItemDTO item;

    private Integer qty;

}
