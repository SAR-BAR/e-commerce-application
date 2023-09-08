package com.ecom.library.service;

import com.ecom.library.dto.AdminDto;
import com.ecom.library.model.Admin;

public interface AdminService {
    Admin save(AdminDto adminDto);
    Admin findByUsername(String username);
}
