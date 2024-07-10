package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
//import com.mjolnir.yggdrasil.repositories.CountryLanguageIdRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorldService {
    private CityRepository cityRepository;
//    private CountryLanguageIdRepository countryLanguageIdRepository;
    private CountryLanguageRepository countryLanguageRepository;
    private CountryRepository countryRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryLanguageRepository countryLanguageRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
//        this.countryLanguageIdRepository = countryLanguageIdRepository;
        this.countryLanguageRepository = countryLanguageRepository;
        this.countryRepository = countryRepository;
    }

    public boolean deleteCountryByCode(String countryCode) {
        Optional< CountryEntity> country = countryRepository.findById(countryCode);

        if (country.isPresent()) {
            countryRepository.delete(country.get());
            return true;
        } else {
            return false;
        }
    }


}
