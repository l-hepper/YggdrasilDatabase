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
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorldService {
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;
    private CityRepository cityRepository;
//    private CountryLanguageIdRepository countryLanguageIdRepository;
    private CountryLanguageRepository countryLanguageRepository;
    private CountryRepository countryRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryLanguageRepository countryLanguageRepository, CountryRepository countryRepository, DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration) {
        this.cityRepository = cityRepository;
//        this.countryLanguageIdRepository = countryLanguageIdRepository;

        this.countryLanguageRepository = countryLanguageRepository;
        this.countryRepository = countryRepository;
        this.dataSourceTransactionManagerAutoConfiguration = dataSourceTransactionManagerAutoConfiguration;
    }

    public boolean deleteCountryByCode(String countryCode) {
        Optional<CountryEntity> country = countryRepository.findById(countryCode);

        if (country.isPresent()) {
            countryRepository.delete(country.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteCityById(int cityID) {
        Optional<CityEntity> city = cityRepository.findById(cityID);

        if (city.isPresent()) {
            cityRepository.delete(city.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteLanguageByCountryCodeAndLanguage(String countryCode, String language) {
        CountryLanguageIdEntity primaryKey = new CountryLanguageIdEntity();
        primaryKey.setCountryCode(countryCode);
        primaryKey.setLanguage(language);

        Optional<CountryLanguageEntity> countryLanguage = countryLanguageRepository.findById(primaryKey);
        if (countryLanguage.isPresent()) {
            countryLanguageRepository.delete(countryLanguage.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteLanguageByLanguage(String languageToDelete) {
        List<CountryLanguageEntity> countryLanguages = countryLanguageRepository.findAll();

        for (CountryLanguageEntity language : countryLanguages) {
            if (language.getId().getLanguage().equals(languageToDelete)) {
                countryLanguageRepository.delete(language);
            }
        }

        List<CountryLanguageEntity> newCountryLanguages = countryLanguageRepository.findAll();
        if (newCountryLanguages.size() < countryLanguages.size()) {
            return true;
        } else {
            return false;
        }
    }

    public List<CountryEntity> getAllCountriesWithoutAHeadOfState() {
        List<CountryEntity> countriesWithoutAHeadOfState = new ArrayList<>();

        List<CountryEntity> countries = countryRepository.findAll();
        for (CountryEntity country : countries) {
            if (country.getHeadOfState() == null || country.getHeadOfState().isBlank()) {
                countriesWithoutAHeadOfState.add(country);
            }
        }

        return countriesWithoutAHeadOfState;
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
