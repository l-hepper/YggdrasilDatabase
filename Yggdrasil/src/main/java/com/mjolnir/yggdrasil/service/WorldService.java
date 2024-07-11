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

import java.math.BigDecimal;
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

    public int getHowManyPeopleSpeakOfficialLanguageIn(String countryCode) {
        Optional<CountryEntity> country = countryRepository.findById(countryCode);

        if (!country.isPresent()) {
            return 0;
        }

        int population = country.get().getPopulation();

        List<CountryLanguageEntity> officialLanguages = getOfficialLanguagesIn(countryCode);
        Optional<CountryLanguageEntity> mostSpoken = getMostSpokenLanguage(officialLanguages);
        if (mostSpoken.isPresent()) {
            int numberOfPeopleWhoSpeakMostPopularOfficialLanguage = (int) ((population / 100) * mostSpoken.get().getPercentage().doubleValue());
            return numberOfPeopleWhoSpeakMostPopularOfficialLanguage;
        } else {
            return 0;
        }
    }

    private List<CountryLanguageEntity> getOfficialLanguagesIn(String countryCode) {
        List<CountryLanguageEntity> languages = countryLanguageRepository.findAll();
        List<CountryLanguageEntity> officialLanguages = new ArrayList<>();
        for (CountryLanguageEntity language : languages) {
            if (language.getId().getCountryCode().equals(countryCode) && language.getIsOfficial().equals("T")) {
                officialLanguages.add(language);
            }
        }
        return officialLanguages;
    }

    private Optional<CountryLanguageEntity> getMostSpokenLanguage(List<CountryLanguageEntity> languages) {
        if (languages.isEmpty()) {
            return Optional.empty();
        }
        CountryLanguageEntity mostSpokenLanguage = languages.get(0);
        for (CountryLanguageEntity language : languages) {
            if (language.getPercentage().compareTo(mostSpokenLanguage.getPercentage()) > 0) {
                mostSpokenLanguage = language;
            }
        }
        return Optional.of(mostSpokenLanguage);
    }
}
