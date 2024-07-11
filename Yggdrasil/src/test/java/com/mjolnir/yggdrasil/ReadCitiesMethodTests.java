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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReadCitiesMethodTests {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private WorldService worldService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("getCityByIdReturnsCorrectCity")
    void getCityByIdReturnsCorrectCity() {
        System.out.println(worldService.getCityById(1));
    }

    @Test
    @DisplayName("False ID returns exception")
    void falseIdReturnsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> worldService.getCityById(0));
    }

    @Test
    @DisplayName("Get all cities returns all cities")
    void getAllCitiesReturnsAllCities() {
        Assertions.assertEquals(4079, worldService.getAllCities().size());
    }

    @Test
    @DisplayName("Correct country code returns all cities with that country code")
    void correctCountryCodeReturnsAllCitiesWithThatCountryCode() {
        Assertions.assertEquals(57, worldService.getCitiesByCountryCode("ARG").size());
        Assertions.assertEquals(3, worldService.getCitiesByCountryCode("ARM").size());
        Assertions.assertEquals(14, worldService.getCitiesByCountryCode("AUS").size());
        Assertions.assertEquals(250, worldService.getCitiesByCountryCode("BRA").size());
    }

    @Test
    @DisplayName("False country code returns exception")
    void falseCountryCodeReturnsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> worldService.getCitiesByCountryCode("ZZZ"));
    }

    @Test
    @DisplayName("Partial Name Match Returns all cities with partial match")
    void partialNameMatchReturnsAllCitiesWithPartialMatch() {
        Assertions.assertEquals(4, worldService.getCitiesByName("pool").size());
    }

    @Test
    @DisplayName("Exact name returns exactly 1 city with that name")
    void exactNameReturnsExactly1CityWithThatName() {
        Assertions.assertEquals(1, worldService.getCitiesByName("Liverpool").size());
    }

    @Test
    @DisplayName("Returned list is empty if there is no cities with a match")
    void returnedListIsEmptyIfThereIsNoCitiesWithAMatch() {
        Assertions.assertEquals(0, worldService.getCitiesByName("asfgasfdasfda").size());
    }

    @Test
    @DisplayName("Returned list is empty if the parameter is empty")
    void returnedListIsEmptyIfTheParameterIsEmpty() {
        Assertions.assertEquals(0, worldService.getCitiesByName("").size());
    }

    @Test
    @DisplayName("Exact district name returns 1 city with that district name")
    void exactDistrictNameReturns1CityWithThatDistrictName() {
        Assertions.assertEquals(1, worldService.getCitiesByDistrict("Alaska").size());
    }

    @Test
    @DisplayName("Partial district name match returns all cities with that partial match")
    void partialDistrictNameMatchReturnsAllCitiesWithThatPartialMatch() {
        Assertions.assertEquals(12, worldService.getCitiesByDistrict("son").size());
        Assertions.assertEquals(11, worldService.getCitiesByDistrict("ton").size());
        Assertions.assertEquals(3, worldService.getCitiesByDistrict("lem").size());
    }

    @Test
    @DisplayName("Returned list is empty if there is no match")
    void returnedListIsEmptyIfThereIsNoMatch() {
        Assertions.assertEquals(0, worldService.getCitiesByName("fdafdafdafda").size());
    }

    @Test
    @DisplayName("Returned list is empty if there empty parameter")
    void returnedListIsEmptyIfThereEmptyParameter() {
        Assertions.assertEquals(0, worldService.getCitiesByName("").size());
    }

    @Test
    @DisplayName("get cities by min population returns all cities with a population above that number")
    void getCitiesByMinPopulationReturnsAllCitiesWithAPopulationAboveThatNumber() {
        Assertions.assertEquals(24, worldService.getCitiesByMinPopulation(5000000).size());
        Assertions.assertEquals(2826, worldService.getCitiesByMinPopulation(123456).size());
        Assertions.assertEquals(4033, worldService.getCitiesByMinPopulation(12345).size());
        Assertions.assertEquals(0, worldService.getCitiesByMinPopulation(12345678).size());
    }

    @Test
    @DisplayName("Population below zero throws exception for getCitiesByMinPopulation")
    void populationBelowZeroThrowsExceptionForGetCitiesByMinPopulation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> worldService.getCitiesByMinPopulation(-1));
    }

    @Test
    @DisplayName("Get cities by max population returns a list of all cities with a population below that number")
    void getCitiesByMaxPopulationReturnsAListOfAllCitiesWithAPopulationBelowThatNumber() {
        Assertions.assertEquals(11, worldService.getCitiesByMaxPopulation(1000).size());
        Assertions.assertEquals(4, worldService.getCitiesByMaxPopulation(500).size());
        Assertions.assertEquals(4, worldService.getCitiesByMaxPopulation(500).size());
        Assertions.assertEquals(4079, worldService.getCitiesByMaxPopulation(100000000).size());
    }

    @Test
    @DisplayName("Population below zero throws exception for getCitiesByMaxPopulation")
    void populationBelowZeroThrowsExceptionForGetCitiesByMaxPopulation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> worldService.getCitiesByMinPopulation(-1));
    }
    
    







}
