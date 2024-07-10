package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
public class DeletionTests {

    @Autowired
    private WorldService worldService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void testDeleteCountryByCode() {
        worldService.deleteCountryByCode("AUS");
        boolean exists = countryRepository.existsById("AUS");
        Assertions.assertFalse(exists);
    }

    @Test
    void testDeleteCountryByCodeReturnsFalseForNonExistentCountry() {
        boolean isDeleted = worldService.deleteCountryByCode("XXX");
        Assertions.assertFalse(isDeleted);
    }

    @Test
    void testMethodReturnsTrueOnSuccessfulDeletion() {
        boolean isDeletionSuccessful = worldService.deleteCountryByCode("AUS");
        Assertions.assertTrue(isDeletionSuccessful);
    }

    @Test
    void testMethodReturnsFalseOnUnsuccessfulDeletion() {
        boolean isDeletionSuccessful = worldService.deleteCountryByCode("XXX");
        Assertions.assertFalse(isDeletionSuccessful);
    }

    @Test
    void testAllForeignKeyRecordsInCityTableAreDeleted() {
        worldService.deleteCountryByCode("AUS");
        List<Integer> listOfCitiesToBeDeleted = new ArrayList(List.of(130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143));
        List<CityEntity> cities = cityRepository.findAllById(listOfCitiesToBeDeleted);
        Assertions.assertTrue(cities.isEmpty());
    }

    @Test
    void testDeleteCityById() {
        worldService.deleteCityById(119);
        boolean exists = cityRepository.existsById(119);
        Assertions.assertFalse(exists);
    }

    @Test
    void testDeleteCityByIdReturnsFalseForNonExistentCity() {
        worldService.deleteCityById(999);
        boolean isDeleted = worldService.deleteCityById(999);
        Assertions.assertFalse(isDeleted);
    }

    @Test
    void testDeleteCityReturnsTrueOnSuccessfulDeletion() {
        boolean isDeletionSuccessful = worldService.deleteCityById(119);
        Assertions.assertTrue(isDeletionSuccessful);
    }


}
