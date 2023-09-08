package com.ecom.library.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*  ----------------------------------Country Model----------------------------------------    */

@Data
@Entity
@Table(name = "country")
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    private String name;
}
