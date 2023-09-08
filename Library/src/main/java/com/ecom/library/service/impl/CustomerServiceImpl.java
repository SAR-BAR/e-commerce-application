package com.ecom.library.service.impl;

import com.ecom.library.dto.CustomerDto;
import com.ecom.library.model.Customer;
import com.ecom.library.repository.CustomerRepository;
import com.ecom.library.repository.RoleRepository;
import com.ecom.library.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

/*  ----------------------------------Customer Service Implementation----------------------------------------    */

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    /*  ----------------------------------Save customer----------------------------------------------------    */
    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(customerDto.getPassword());
        customer.setUsername(customerDto.getUsername());
        customer.setRoles(Collections.singletonList(roleRepository.findByName("CUSTOMER")));
        return customerRepository.save(customer);
    }

    /*  ----------------------------------Find customer by username----------------------------------------------------    */
    @Override
    public Customer findByUsername(String username) {

        return customerRepository.findByUsername(username);
    }

    /*  ----------------------------------Find customer ----------------------------------------------------    */
    @Override
    public CustomerDto getCustomer(String username) {
        CustomerDto customerDto = new CustomerDto();
        Customer customer = customerRepository.findByUsername(username);
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setCity(customer.getCity());
        customerDto.setCountry(customer.getCountry());
        return customerDto;
    }

    /*  ----------------------------------Change password----------------------------------------------------    */
    @Override
    public Customer changePass(CustomerDto customerDto) {
        //Find customer by username
        Customer customer = customerRepository.findByUsername(customerDto.getUsername());
        //set password to password received from Dto object
        customer.setPassword(customerDto.getPassword());
        return customerRepository.save(customer);
    }

    /*  ----------------------------------Update customer----------------------------------------------------    */
    @Override
    public Customer update(CustomerDto dto) {
        Customer customer = customerRepository.findByUsername(dto.getUsername());
        //Update - address, city, country, phone number
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setCountry(dto.getCountry());
        customer.setPhoneNumber(dto.getPhoneNumber());
        return customerRepository.save(customer);
    }

}
