package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class YggdrasilApplicationTests {

    @Test
    @DisplayName("Check context loads successfully")
    void contextLoads() {
    }

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryLanguageRepository countryLanguageRepository;
    @Autowired
    private WorldService worldService;

    @Test
    @DisplayName("Check createNewCity service method adds a new city")
    void checkCreateNewCityServiceMethodAddsANewCity() {
        worldService.createNewCity("USA", "Lewis's City", "Lewis's District", 1234567890, false);
        Optional<CityEntity> cityEntityOptional = cityRepository.findByName("Lewis's City");
        assertTrue(cityEntityOptional.isPresent());
    }

    @Test
    @DisplayName("Check createNewCity service method doesn't add new city with invalid country code")
    void checkCreateNewCityServiceMethodDoesntAddNewCityWithInvalidCountryCode() {
        worldService.createNewCity("ZZZ", "Lewis's City", "Lewis's District", 1234567890, false);
        Optional<CityEntity> city = cityRepository.findByName("Lewis's City");
        assertFalse(city.isPresent());
    }

    @Test
    @DisplayName("Check createNewCity service method doesn't add new city with null city name")
    void checkCreateNewCityServiceMethodDoesntAddNewCityWithNullCityName() {
        worldService.createNewCity("USA", null, "District", 12345, false);
        Optional<CityEntity> city = cityRepository.findByName(null);
        assertFalse(city.isPresent());
    }

    @Test
    @DisplayName("Check createNewCity service method doesn't add new city with null district")
    void checkCreateNewCityServiceMethodDoesntAddNewCityWithNullDistrict() {
        worldService.createNewCity("USA", "Lewis's City", null, 12345, false);
        Optional<CityEntity> city = cityRepository.findByName("Lewis's City");
        assertFalse(city.isPresent());
    }

    @Test
    @DisplayName("Check createNewCity service method doesn't add new city with invalid population")
    void checkCreateNewCityServiceMethodDoesntAddNewCityWithInvalidPopulation() {
        worldService.createNewCity("USA", "Lewis's City", "District", -1, false);
        Optional<CityEntity> city = cityRepository.findByName("Lewis's City");
        assertFalse(city.isPresent());
    }


    @Test
    @DisplayName("Create new country with valid data and no capital")
    void testCreateNewCountryWithoutCapital() {
        String countryCode = "XYZ";
        String countryName = "TestCountry";
        String continent = "Asia";
        String region = "Southeast Asia";
        BigDecimal surfaceArea = new BigDecimal("100000");
        Short independenceYear = 2000;
        Integer population = 1000000;
        BigDecimal lifeExpectancy = new BigDecimal("75.5");
        BigDecimal gnp = new BigDecimal("500000");
        BigDecimal gnpOld = new BigDecimal("400000");
        String localName = "TestCountry";
        String governmentForm = "Republic";
        String headOfState = "Test Leader";
        String countryCode2 = "ZZ";
        boolean hasACapital = false;

        worldService.createNewCountry(countryCode, countryName, continent, region, surfaceArea, independenceYear,
                population, lifeExpectancy, gnp, gnpOld, localName, governmentForm, headOfState, countryCode2,
                hasACapital);

        Optional<CountryEntity> country = countryRepository.findById(countryCode);
        assertTrue(country.isPresent());


    }


    @Test
    @DisplayName("Create new country with valid data and a capital")
    void testCreateNewCountryWithCapital() {
        String countryCode = "XYZ";
        String countryName = "TestCountry";
        String continent = "Asia";
        String region = "Southeast Asia";
        BigDecimal surfaceArea = new BigDecimal("100000");
        Short independenceYear = 2000;
        Integer population = 1000000;
        BigDecimal lifeExpectancy = new BigDecimal("75.5");
        BigDecimal gnp = new BigDecimal("500000");
        BigDecimal gnpOld = new BigDecimal("400000");
        String localName = "TestCountry";
        String governmentForm = "Republic";
        String headOfState = "Test Leader";
        String countryCode2 = "ZZ";
        boolean hasACapital = true;

        worldService.createNewCountry(countryCode, countryName, continent, region, surfaceArea, independenceYear,
                population, lifeExpectancy, gnp, gnpOld, localName, governmentForm, headOfState, countryCode2,
                hasACapital);

        CountryEntity countryEntity = countryRepository.findById(countryCode).orElse(null);

        assert countryEntity != null;
        String confirmCountryName = countryEntity.getName();
        assertEquals(confirmCountryName, countryName);
        assertNotNull(countryEntity);
        assertNotNull(countryEntity.getCapital());

        Optional<CountryEntity> country = countryRepository.findById(countryCode);
        assertTrue(country.isPresent());
    }

}
