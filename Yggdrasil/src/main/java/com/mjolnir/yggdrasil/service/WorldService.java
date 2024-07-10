package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.mjolnir.yggdrasil.utilities.Regex.CONTINENT_REGEX;
import static com.mjolnir.yggdrasil.utilities.Regex.REGION_REGEX;

@Service
public class WorldService {
    private final CityRepository cityRepository;
    private final CountryLanguageRepository countryLanguageRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryLanguageRepository countryLanguageRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryLanguageRepository = countryLanguageRepository;
        this.countryRepository = countryRepository;
    }

    public void createNewCity(String countryCode, String cityName, String cityDistrict, Integer population, boolean isCapital) {
        Optional<CountryEntity> countryEntityOptional = countryRepository.findById(countryCode);
        if (countryEntityOptional.isPresent()
                && cityName != null
                && cityDistrict != null
                && population > 0) {

            CountryEntity countryEntity = countryEntityOptional.get();

            CityEntity cityEntity = new CityEntity();
            cityEntity.setName(cityName);
            cityEntity.setCountryCode(countryEntity);
            cityEntity.setDistrict(cityDistrict);
            cityEntity.setPopulation(population);
            cityRepository.save(cityEntity);

            //What if there is already a capital?
            if (isCapital) {
                countryEntity.setCapital(cityEntity.getId());
                countryRepository.save(countryEntity);
            }
        }
    }

    public void createNewCountry(String countryCode, String countryName, String continent, String region, BigDecimal surfaceArea, Short independenceYear, Integer population, BigDecimal lifeExpectancy, BigDecimal GNP, BigDecimal GNPOld, String localName, String governmentForm, String headOfState, String countryCode2, boolean hasACapital) {
        String cityDistrict = "";
        String cityName = "";
        boolean countryCodeExists = countryRepository.existsById(countryCode);
        boolean countryCode2Exists = countryRepository.existsByCode2(countryCode2);

        if (!countryCodeExists && !countryCode2Exists
                && countryName != null
                && continent.matches(CONTINENT_REGEX)
                && region.matches(REGION_REGEX)
                && surfaceArea.compareTo(BigDecimal.ZERO) > 0
                && (independenceYear == null || independenceYear > 0)
                && (population == null || population > 0)
                && (lifeExpectancy == null || lifeExpectancy.compareTo(BigDecimal.ZERO) > 0)
                && (GNP == null || GNP.compareTo(BigDecimal.ZERO) > 0)
                && (GNPOld == null || GNPOld.compareTo(BigDecimal.ZERO) >= 0)
                && localName != null
                && governmentForm != null
                && (headOfState == null || !headOfState.isEmpty())
                && (countryCode2 != null && !countryCode2.isEmpty())) {

            CountryEntity newCountry = new CountryEntity();
            newCountry.setCode(countryCode);
            newCountry.setName(countryName);
            newCountry.setContinent(continent);
            newCountry.setRegion(region);
            newCountry.setSurfaceArea(surfaceArea);
            newCountry.setIndepYear(independenceYear);
            newCountry.setPopulation(population);
            newCountry.setLifeExpectancy(lifeExpectancy);
            newCountry.setGnp(GNP);
            newCountry.setGNPOld(GNPOld);
            newCountry.setLocalName(localName);
            newCountry.setGovernmentForm(governmentForm);
            newCountry.setHeadOfState(headOfState);
            newCountry.setCode2(countryCode2);

            countryRepository.save(newCountry);

            if (hasACapital) {
                createNewCity(countryCode, cityName, cityDistrict, population, true);
            }
        }
    }
}
