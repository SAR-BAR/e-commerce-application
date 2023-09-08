package com.ecom.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*  ----------------------------------Cart Item Model----------------------------------------    */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {

    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Many to many mapping of Cart item with Shopping cart
    //Many shopping carts can contain many cart items
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "shopping_cart_id")
    private ShoppingCart cart;

    //One to one mapping of Cart item with Product
    // One item can be one product
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    private int quantity;
    private double unitPrice;

    @Override
    public String toString(){
        return "CartItem{" +
                "id = "+ id +
                ", cart = "+ cart.getId() +
                ", product = "+ product.getName() +
                ", quantity = "+ quantity +
                ", unitPrice = "+ unitPrice +
                ", totalPrice = "+ '}';
    }
}
