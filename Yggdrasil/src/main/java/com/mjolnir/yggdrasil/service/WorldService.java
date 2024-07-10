package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorldService {
    private CityRepository cityRepository;
    private CountryLanguageRepository countryLanguageRepository;
    private CountryRepository countryRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryLanguageRepository countryLanguageRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryLanguageRepository = countryLanguageRepository;
        this.countryRepository = countryRepository;
    }


    public boolean createNewCity(String countryCode, String cityName, String cityDistrict, int population) {
        Optional<CountryEntity> countryEntity = countryRepository.findById(countryCode);
        if (countryEntity.isPresent() && cityName != null && cityDistrict != null && population > 0) {
            CityEntity cityEntity = new CityEntity();
            cityEntity.setName(cityName);
            cityEntity.setCountryCode(countryEntity.get());
            cityEntity.setDistrict(cityDistrict);
            cityEntity.setPopulation(population);
            cityRepository.save(cityEntity);
            return true;
        }
        return false;
    }
}
