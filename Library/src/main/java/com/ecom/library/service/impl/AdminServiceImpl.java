package com.ecom.library.service.impl;

import com.ecom.library.dto.AdminDto;
import com.ecom.library.model.Admin;
import com.ecom.library.repository.AdminRepository;
import com.ecom.library.repository.RoleRepository;
import com.ecom.library.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;


/*  ----------------------------------Admin Service Implementation----------------------------------------    */

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    private final RoleRepository roleRepository;


    /*  ----------------------------------Save Admin----------------------------------------------------    */
    @Override
    public Admin save(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setRoles(Collections.singletonList(roleRepository.findByName("ADMIN")));
        return adminRepository.save(admin);
    }

    /*  ----------------------------------Find Admin by username----------------------------------------    */
    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
}
