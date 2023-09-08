package com.ecom.library.service.impl;

import com.ecom.library.model.Country;
import com.ecom.library.repository.CountryRepository;
import com.ecom.library.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*  ----------------------------------Country Service Implementation----------------------------------------    */

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    /*  ----------------------------------Find all countries----------------------------------------------------    */
    @Override
    public List<Country> findAll() {

        return countryRepository.findAll();
    }
}
