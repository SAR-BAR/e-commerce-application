package com.ecom.library.dto;

import com.ecom.library.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/*  ----------Shopping Cart Dto-----------    */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
    private Long id;
    private Customer customer;
    private double totalPrice;
    private int totalItems;
    private Set<CartItemDto> cartItems;
}
