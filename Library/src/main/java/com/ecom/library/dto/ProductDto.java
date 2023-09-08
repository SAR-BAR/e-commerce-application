package com.ecom.library.dto;


import com.ecom.library.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*  ----------Product Dto-----------    */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int currentQuantity;
    private double salePrice;
    private double costPrice;
    private String image;
    private Category category;
    private boolean activated;
    private boolean deleted;
}
