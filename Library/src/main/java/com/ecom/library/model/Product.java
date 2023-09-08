package com.ecom.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*  ----------------------------------Product Model----------------------------------------    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//Unique constraints on name and image
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "image"}))
public class Product {
    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")

    private Long id;
    private String name;
    private String description;
    private int currentQuantity;
    private double costPrice;
    private double salePrice;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    //Many to one mapping of Product with category
    //Many products belong to same category
    //Inverse entity of mapping
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    private boolean is_activated;
    private boolean is_deleted;

}
