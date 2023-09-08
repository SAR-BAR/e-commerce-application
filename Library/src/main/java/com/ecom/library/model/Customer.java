package com.ecom.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*  ----------------------------------Customer Model----------------------------------------    */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Entity
//Unique constraints on username
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Customer {

    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;

    //One to one mapping of Customer with city
    //One customer can be associated with one city only
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name", referencedColumnName = "id")
    private City city;

    private String country;

    //Many to many mapping of Customer with Role
    //Different types of customer can have many roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "customer_role", joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;

    //One to one bidirectional mapping
    //A customer owns only one shopping cart
    //Owning entity of mapping
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart cart;

    //One to one bidirectional mapping
    //A customer can have many orders
    //Owning entity of mapping
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    public Customer() {
        this.country = "IN";
        this.cart = new ShoppingCart();
        this.orders = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", city=" + city.getName() +
                ", country='" + country + '\'' +
                ", roles=" + roles +
                ", cart=" + cart.getId() +
                ", orders=" + orders.size() +
                '}';
    }
}
