package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CreateMethodTests {

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

        Assertions.assertEquals(countryEntity.getCapital(), cityRepository.findAll().getLast().getId());
    }

    @Test
    @DisplayName("Create a new country doesnt create a new country with an existing country code ")
    void createANewCountryDoesntCreateANewCountryWithAnExistingCountryCode() {
        String countryCode = "USA";
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

        CountryEntity countryWhichWouldBeAdded = countryRepository.findAll().getLast();
        String expected = "TestCountry";
        Assertions.assertNotEquals(expected, countryWhichWouldBeAdded.getName());


    }

    @Test
    @DisplayName("createNewCountryDoesNotAddNewCountryIfCode2Exists")
    void createNewCountryDoesNotAddNewCountryIfCode2Exists() {
        String countryCode = "ZZZ";
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
        String countryCode2 = "US";
        boolean hasACapital = false;

        worldService.createNewCountry(countryCode, countryName, continent, region, surfaceArea, independenceYear,
                population, lifeExpectancy, gnp, gnpOld, localName, governmentForm, headOfState, countryCode2,
                hasACapital);

        CountryEntity countryWhichWouldBeAdded = countryRepository.findAll().getLast();

        String expected = "TestCountry";
        Assertions.assertNotEquals(expected, countryWhichWouldBeAdded.getName());
    }

    @Test
    @DisplayName("createNewCountryDoesntAcceptNegativePopulation")
    void createNewCountryDoesntAcceptNegativePopulation() {
        String countryCode = "ZZZ";
        String countryName = "TestCountry";
        String continent = "Asia";
        String region = "Southeast Asia";
        BigDecimal surfaceArea = new BigDecimal("100000");
        Short independenceYear = 2000;
        Integer population = -1;
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

        CountryEntity countryWhichWouldBeAdded = countryRepository.findAll().getLast();

        String expected = "TestCountry";
        Assertions.assertNotEquals(expected, countryWhichWouldBeAdded.getName());
    }

    @Test
    @DisplayName("Create new country language with valid parameters")
    void createNewCountryLanguageWithValidParameters() {
        String countryCode = "ZWE";
        String countryLanguage = "TestCountryLanguage";
        String isOfficial = "F";
        BigDecimal percentageSpoken = new BigDecimal("50");

        worldService.createNewCountryLanguage(countryCode, countryLanguage, isOfficial, percentageSpoken);

        CountryLanguageEntity country = countryLanguageRepository.findAll().getLast();
        String expected = "TestCountryLanguage";
        Assertions.assertEquals(expected, country.getLanguage());
    }

    @Test
    @DisplayName("Doesnt create new country language for invalid country code")
    void doesntCreateNewCountryLanguageForInvalidCountryCode() {
        String countryCode = "ZZZ";
        String countryLanguage = "TestCountryLanguage";
        String isOfficial = "F";
        BigDecimal percentageSpoken = new BigDecimal("50");

        worldService.createNewCountryLanguage(countryCode, countryLanguage, isOfficial, percentageSpoken);

        CountryLanguageEntity country = countryLanguageRepository.findAll().getLast();
        String expected = "TestCountryLanguage";
        Assertions.assertNotEquals(expected, country.getLanguage());
    }

    @Test
    @DisplayName("Doesnt create a new country language with zero percent spoken")
    void doesntCreateANewCountryLanguageWithZeroPercentSpoken() {
        String countryCode = "ZWE";
        String countryLanguage = "TestCountryLanguage";
        String isOfficial = "F";
        BigDecimal percentageSpoken = new BigDecimal("0");

        worldService.createNewCountryLanguage(countryCode, countryLanguage, isOfficial, percentageSpoken);

        CountryLanguageEntity country = countryLanguageRepository.findAll().getLast();
        String expected = "TestCountryLanguage";
        Assertions.assertNotEquals(expected, country.getLanguage(), country.getLanguage());
    }

    @Test
    @DisplayName("Doesnt create a new country language with over 100 percent spoken")
    void doesntCreateANewCountryLanguageWithOver100PercentSpoken() {
        String countryCode = "ZWE";
        String countryLanguage = "TestCountryLanguage";
        String isOfficial = "F";
        BigDecimal percentageSpoken = new BigDecimal("101");

        worldService.createNewCountryLanguage(countryCode, countryLanguage, isOfficial, percentageSpoken);

        CountryLanguageEntity country = countryLanguageRepository.findAll().getLast();
        String expected = "TestCountryLanguage";
        Assertions.assertNotEquals(expected, country.getLanguage(), country.getLanguage());
    }

    @Test
    @DisplayName("Creates new country language with minimum percent spoken")
    void createsNewCountryLanguageWithMinimumPercentSpoken() {
        String countryCode = "ZWE";
        String countryLanguage = "TestCountryLanguage";
        String isOfficial = "F";
        BigDecimal percentageSpoken = new BigDecimal("0.1");

        worldService.createNewCountryLanguage(countryCode, countryLanguage, isOfficial, percentageSpoken);

        CountryLanguageEntity country = countryLanguageRepository.findAll().getLast();
        String expected = "TestCountryLanguage";
        Assertions.assertEquals(expected, country.getLanguage());
    }

    @Test
    @DisplayName("Creates new country language with 100 percent spoken")
    void createsNewCountryLanguageWith100PercentSpoken() {
        String countryCode = "ZWE";
        String countryLanguage = "TestCountryLanguage";
        String isOfficial = "F";
        BigDecimal percentageSpoken = new BigDecimal("100");

        worldService.createNewCountryLanguage(countryCode, countryLanguage, isOfficial, percentageSpoken);

        CountryLanguageEntity country = countryLanguageRepository.findAll().getLast();
        String expected = "TestCountryLanguage";
        Assertions.assertEquals(expected, country.getLanguage());
    }

    @Test
    @DisplayName("Doesnt create new country if isOfficial is not T or F")
    void doesntCreateNewCountryIfIsOfficialIsNotTorF() {
        String countryCode = "ZWE";
        String countryLanguage = "TestCountryLanguage";
        String isOfficial = "X";
        BigDecimal percentageSpoken = new BigDecimal("50");

        worldService.createNewCountryLanguage(countryCode, countryLanguage, isOfficial, percentageSpoken);

        CountryLanguageEntity country = countryLanguageRepository.findAll().getLast();
        String expected = "TestCountryLanguage";
        Assertions.assertNotEquals(expected, country.getLanguage());
    }

}
