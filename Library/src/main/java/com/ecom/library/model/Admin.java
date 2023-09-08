package com.ecom.library.model;

import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*  ----------------------------------Admin Model----------------------------------------    */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
//unique constraints for username and image
@Table(name="admins", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "image"}))
public class Admin {

    //Primary key - automatic generation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="admin_id")
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String image;


    //Many to many mapping between Admin and Roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name ="admins_roles", joinColumns = @JoinColumn(name ="admin_id",referencedColumnName="admin_id" ),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "role_id")
    )
    private List<Role> roles;
}
