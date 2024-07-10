package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageIdEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
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
public class DeletionMethodsTests {

    @Autowired
    private WorldService worldService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryLanguageRepository countryLanguageRepository;

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
    void testAllForeignKeyRecordsInCountryLanguageTableAreDeleted() {
        CountryLanguageIdEntity arabic = new CountryLanguageIdEntity();
        arabic.setCountryCode("AUS");
        arabic.setLanguage("Arabic");

        CountryLanguageIdEntity cantonChinese = new CountryLanguageIdEntity();
        cantonChinese.setCountryCode("AUS");
        cantonChinese.setLanguage("Canton Chinese");

        CountryLanguageIdEntity english = new CountryLanguageIdEntity();
        english.setCountryCode("AUS");
        english.setLanguage("English");

        CountryLanguageIdEntity german = new CountryLanguageIdEntity();
        german.setCountryCode("AUS");
        german.setLanguage("German");

        CountryLanguageIdEntity greek = new CountryLanguageIdEntity();
        greek.setCountryCode("AUS");
        greek.setLanguage("Greek");

        CountryLanguageIdEntity italian = new CountryLanguageIdEntity();
        italian.setCountryCode("AUS");
        italian.setLanguage("Italian");

        CountryLanguageIdEntity serboCroatian = new CountryLanguageIdEntity();
        serboCroatian.setCountryCode("AUS");
        serboCroatian.setLanguage("Serbo-Croatian");

        CountryLanguageIdEntity vietnamese = new CountryLanguageIdEntity();
        vietnamese.setCountryCode("AUS");
        vietnamese.setLanguage("Vietnamese");

        worldService.deleteCountryByCode("AUS");
        List<CountryLanguageEntity> list = countryLanguageRepository.findAllById(
                new ArrayList(
                        List.of(
                                arabic,
                                cantonChinese,
                                english,
                                german,
                                greek,
                                italian,
                                serboCroatian,
                                vietnamese)));
        Assertions.assertTrue(list.isEmpty());
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

    @Test
    void testDeleteLanguageByCountryCodeAndLanguage() {
        worldService.deleteLanguageByCountryCodeAndLanguage("AUS", "Arabic");
        CountryLanguageIdEntity primaryKey = new CountryLanguageIdEntity();
        primaryKey.setCountryCode("AUS");
        primaryKey.setLanguage("Arabic");
        boolean exists = countryLanguageRepository.existsById(primaryKey);
        Assertions.assertFalse(exists);
    }

    @Test
    void testDeleteLanguageByCountryCodeAndLanguageReturnsFalseForNonExistentLanguage() {
        boolean isDeleted = worldService.deleteLanguageByCountryCodeAndLanguage("AUS", "Tongan");
        Assertions.assertFalse(isDeleted);
    }

    @Test
    void testDeleteLanguageByCountryCodeAndLanguageReturnsFalseForNonExistentCountry() {
        boolean isDeleted = worldService.deleteLanguageByCountryCodeAndLanguage("XXX", "Tongan");
        Assertions.assertFalse(isDeleted);
    }

    @Test
    void testDeleteLanguageByLanguageDeletesAllRelevantLanguageRecords() {
        worldService.deleteLanguageByLanguage("Tongan");

        CountryLanguageIdEntity tonganInASM = new CountryLanguageIdEntity();
        tonganInASM.setCountryCode("ASM");
        tonganInASM.setLanguage("Tongan");

        CountryLanguageIdEntity tonganInTON = new CountryLanguageIdEntity();
        tonganInTON.setCountryCode("TON");
        tonganInTON.setLanguage("Tongan");

        boolean exists = countryLanguageRepository.existsById(tonganInASM);
        Assertions.assertFalse(exists);
    }

    @Test
    void testDeleteLanguageByLanguageReturnsFalseForNonExistentLanguage() {
        boolean isDeleted = worldService.deleteLanguageByLanguage("Klingon");
        Assertions.assertFalse(isDeleted);
    }

    @Test
    void testDeleteLanguageByLanguageReturnsTrueForExistentLanguage() {
        boolean isDeleted = worldService.deleteLanguageByLanguage("Arabic");
        Assertions.assertTrue(isDeleted);
    }
}
