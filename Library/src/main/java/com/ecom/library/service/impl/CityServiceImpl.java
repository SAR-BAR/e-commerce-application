package com.ecom.library.service.impl;

import com.ecom.library.repository.CityRepository;
import com.ecom.library.service.CityService;
import com.ecom.library.model.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*  ----------------------------------City Service Implementation----------------------------------------    */
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    /*  ----------------------------------Find all cities----------------------------------------------------    */
    @Override
    public List<City> findAll() {

        return cityRepository.findAll();
    }
}
