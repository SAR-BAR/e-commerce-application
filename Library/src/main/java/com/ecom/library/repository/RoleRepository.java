package com.ecom.library.repository;

import com.ecom.library.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository {
    Role findByName(String name);
}
