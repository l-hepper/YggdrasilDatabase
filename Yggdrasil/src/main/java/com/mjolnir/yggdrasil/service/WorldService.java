package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageIdEntity;
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
    //private CountryLanguageIdRepository countryLanguageIdRepository;
    private CountryLanguageRepository countryLanguageRepository;
    private CountryRepository countryRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryLanguageRepository countryLanguageRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        //this.countryLanguageIdRepository = countryLanguageIdRepository;
        this.countryLanguageRepository = countryLanguageRepository;
        this.countryRepository = countryRepository;
    }

    // Update
    public boolean updateCityById(Integer id, CityEntity city){
        if (id == null || city == null) {
            return false;
        }

        Optional<CityEntity> toUpdate = cityRepository.findById(id);
        if (toUpdate.isPresent()) {
            CityEntity existingCity = toUpdate.get();
            existingCity.setName(city.getName());
            existingCity.setDistrict(city.getDistrict());
            existingCity.setPopulation(city.getPopulation());
            cityRepository.save(existingCity);
            return true;
        }
        return false;
    }

    public boolean updateCountryById(String id, CountryEntity country){
        if (id == null || country == null) {
            return false;
        }

        Optional<CountryEntity> toUpdate = countryRepository.findById(id);
        if (toUpdate.isPresent()) {
            CountryEntity existingCountry = toUpdate.get();
            existingCountry.setName(country.getName());
            existingCountry.setPopulation(country.getPopulation());
            existingCountry.setSurfaceArea(country.getSurfaceArea());
            existingCountry.setIndepYear(country.getIndepYear());
            existingCountry.setLifeExpectancy(country.getLifeExpectancy());
            existingCountry.setGnp(country.getGnp());
            existingCountry.setGNPOld(country.getGNPOld());
            existingCountry.setLocalName(country.getLocalName());
            existingCountry.setGovernmentForm(country.getGovernmentForm());
            existingCountry.setHeadOfState(country.getHeadOfState());
            existingCountry.setCapital(country.getCapital());
            existingCountry.setCode2(country.getCode2());
            countryRepository.save(existingCountry);
            return true;
        }
        return false;
    }

    public boolean updateLanguageById(CountryLanguageIdEntity id, CountryLanguageEntity lang){
        if (id == null || lang == null) {
            return false;
        }

        Optional<CountryLanguageEntity> toUpdate = countryLanguageRepository.findById(id);
        if (toUpdate.isPresent()) {
            CountryLanguageEntity existingLanguage = toUpdate.get();
            existingLanguage.setIsOfficial(lang.getIsOfficial());
            existingLanguage.setPercentage(lang.getPercentage());
            countryLanguageRepository.save(existingLanguage);
            return true;
        }
        return false;
    }

}
