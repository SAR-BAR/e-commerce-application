package com.ecom.library.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

/*  ----------------------------------Order Model----------------------------------------    */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Date orderDate;
    private Date deliveryDate;
    private String orderStatus;
    private double totalPrice;
    private double tax;
    private int quantity;
    private String paymentMethod;
    private boolean isAccept;

    //Many to one mapping of Order with customer
    //Many orders can belong to one customer
    //inverse entity of mapping
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;


    //One to many mapping of Order with orderDetails
    //One order can have many order details
    //Owning entity of mapping
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderDetail> orderDetailList;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", totalPrice=" + totalPrice +
                ", tax='" + tax + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customer=" + customer.getUsername() +
                ", orderDetailList=" + orderDetailList.size() +
                '}';
    }
}
