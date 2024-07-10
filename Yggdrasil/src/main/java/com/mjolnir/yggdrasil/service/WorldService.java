package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageIdRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorldService {
    private CityRepository cityRepository;
    private CountryLanguageIdRepository countryLanguageIdRepository;
    private CountryLanguageRepository countryLanguageRepository;
    private CountryRepository countryRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryLanguageIdRepository countryLanguageIdRepository, CountryLanguageRepository countryLanguageRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryLanguageIdRepository = countryLanguageIdRepository;
        this.countryLanguageRepository = countryLanguageRepository;
        this.countryRepository = countryRepository;
    }

    public boolean updateCityById(Integer id, CityEntity city){
        Optional<CityEntity> cityToAlter = cityRepository.findById(id);
        if(cityToAlter.isPresent() && city !=  null){
            cityToAlter.get().setName(city.getName());
            cityToAlter.get().setDistrict(city.getDistrict());
            cityToAlter.get().setPopulation(city.getPopulation());
            cityRepository.save(cityToAlter.get());
            return true;
        }
        return false;
    }

    public boolean updateCityNameById(Integer id, String cityName){
        Optional<CityEntity> cityToAlter = cityRepository.findById(id);
        if(cityToAlter.isPresent() && cityName !=  null){
            cityToAlter.get().setName(cityName);
            cityRepository.save(cityToAlter.get());
            return true;
        }
        return false;
    }

    public boolean updateCityDistrictById(Integer id, String cityDistrict){
        Optional<CityEntity> cityToAlter = cityRepository.findById(id);
        if(cityToAlter.isPresent() && cityDistrict !=  null){
            cityToAlter.get().setDistrict(cityDistrict);
            cityRepository.save(cityToAlter.get());
            return true;
        }
        return false;
    }

    public boolean updateCityPopulationById(Integer id, Integer population){
        Optional<CityEntity> cityToAlter = cityRepository.findById(id);
        if(cityToAlter.isPresent() && population !=  null){
            cityToAlter.get().setPopulation(population);
            cityRepository.save(cityToAlter.get());
            return true;
        }
        return false;
    }

}
