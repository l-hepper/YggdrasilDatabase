package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
public class UpdateTests {

    @Autowired
    private WorldService worldService;

    @MockBean
    private CountryRepository countryRepository;
    @MockBean
    private CityRepository cityRepository;
    @MockBean
    private CountryLanguageRepository countryLanguageRepository;

    // City
    @Test
    public void testUpdateValidCityById() {
        CityEntity newCity = new CityEntity();
        newCity.setName("TestCity");
        newCity.setDistrict("TestDistrict");
        newCity.setPopulation(50000);

        CityEntity existingCity = new CityEntity();
        existingCity.setId(50);

        when(cityRepository.findById(50)).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(any(CityEntity.class))).thenReturn(existingCity);

        Assertions.assertTrue(worldService.updateCityById(50, newCity));
    }

    @Test
    public void testUpdateInvalidCityById() {
        CityEntity newCity = new CityEntity();
        newCity.setName("TestCity");
        newCity.setDistrict("TestDistrict");
        newCity.setPopulation(50000);

        when(cityRepository.findById(5000)).thenReturn(Optional.empty());

        Assertions.assertFalse(worldService.updateCityById(5000, newCity));
    }

    @Test
    public void testUpdateCityWithNullId() {
        CityEntity newCity = new CityEntity();
        newCity.setName("TestCity");
        newCity.setDistrict("TestDistrict");
        newCity.setPopulation(50000);

        Assertions.assertFalse(worldService.updateCityById(null, newCity)); // Invalid
    }

    @Test
    public void testUpdateCityWithNullEntity() {
        Assertions.assertFalse(worldService.updateCityById(50, null));  // Invalid
    }

    @Test
    public void testUpdateCityWithNegativePopulation() {
        CityEntity newCity = new CityEntity();
        newCity.setName("TestCity");
        newCity.setDistrict("TestDistrict");
        newCity.setPopulation(-500);    // Invalid

        Assertions.assertFalse(worldService.updateCityById(50, newCity));
    }

    // Country
    @Test
    public void testUpdateValidCountryById() {
        CountryEntity newCountry = new CountryEntity();
        newCountry.setName("TestCountry");
        newCountry.setPopulation(0);
        newCountry.setSurfaceArea(BigDecimal.valueOf(0));
        newCountry.setIndepYear((short) 0);
        newCountry.setPopulation(0);
        newCountry.setLifeExpectancy(BigDecimal.valueOf(0));
        newCountry.setGnp(BigDecimal.valueOf(0));
        newCountry.setGNPOld(BigDecimal.valueOf(0));
        newCountry.setLocalName("David");
        newCountry.setGovernmentForm("What?");
        newCountry.setHeadOfState("John");
        newCountry.setCapital(0);
        newCountry.setCode2("A2");

        CountryEntity existingCountry = new CountryEntity();
        existingCountry.setCode("USA");

        when(countryRepository.findById("USA")).thenReturn(Optional.of(existingCountry));
        when(countryRepository.save(any(CountryEntity.class))).thenReturn(existingCountry);

        Assertions.assertTrue(worldService.updateCountryById("USA", newCountry));
    }

    @Test
    public void testUpdateInvalidCountryById() {
        CountryEntity newCountry = new CountryEntity();

        newCountry.setName("TestCountry");
        newCountry.setPopulation(0);
        newCountry.setSurfaceArea(BigDecimal.valueOf(0));
        newCountry.setIndepYear((short) 0);
        newCountry.setPopulation(0);
        newCountry.setLifeExpectancy(BigDecimal.valueOf(0));
        newCountry.setGnp(BigDecimal.valueOf(0));
        newCountry.setGNPOld(BigDecimal.valueOf(0));
        newCountry.setLocalName("David");
        newCountry.setGovernmentForm("What?");
        newCountry.setHeadOfState("John");
        newCountry.setCapital(0);
        newCountry.setCode2("A2");

        CountryEntity existingCountry = new CountryEntity();
        existingCountry.setCode("USA");

        when(countryRepository.findById("USA")).thenReturn(Optional.empty());

        Assertions.assertFalse(worldService.updateCountryById("USA", newCountry));  // Invalid
    }

    @Test
    public void testUpdateInvalidCountryWithNullId() {
        CountryEntity newCountry = new CountryEntity();

        newCountry.setName("TestCountry");
        newCountry.setPopulation(0);
        newCountry.setSurfaceArea(BigDecimal.valueOf(0));
        newCountry.setIndepYear((short) 0);
        newCountry.setPopulation(0);
        newCountry.setLifeExpectancy(BigDecimal.valueOf(0));
        newCountry.setGnp(BigDecimal.valueOf(0));
        newCountry.setGNPOld(BigDecimal.valueOf(0));
        newCountry.setLocalName("David");
        newCountry.setGovernmentForm("What?");
        newCountry.setHeadOfState("John");
        newCountry.setCapital(0);
        newCountry.setCode2("A2");

        Assertions.assertFalse(worldService.updateCountryById(null, newCountry));  // Invalid
    }

    @Test
    public void testUpdateInvalidCountryWithNullEntity() {
        CountryEntity newCountry = new CountryEntity();

        newCountry.setName("TestCountry");
        newCountry.setPopulation(0);
        newCountry.setSurfaceArea(BigDecimal.valueOf(0));
        newCountry.setIndepYear((short) 0);
        newCountry.setPopulation(0);
        newCountry.setLifeExpectancy(BigDecimal.valueOf(0));
        newCountry.setGnp(BigDecimal.valueOf(0));
        newCountry.setGNPOld(BigDecimal.valueOf(0));
        newCountry.setLocalName("David");
        newCountry.setGovernmentForm("What?");
        newCountry.setHeadOfState("John");
        newCountry.setCapital(0);
        newCountry.setCode2("A2");

        Assertions.assertFalse(worldService.updateCountryById("USA", null));  // Invalid
    }

    @Test
    public void testUpdateCountryWithNegativeLifeExpectancy() {
        CountryEntity newCountry = new CountryEntity();
        newCountry.setCode("A2U");
        newCountry.setName("TestCountry");
        newCountry.setPopulation(0);
        newCountry.setSurfaceArea(BigDecimal.valueOf(0));
        newCountry.setIndepYear((short) 0);
        newCountry.setLifeExpectancy(BigDecimal.valueOf(-1));  // Invalid
        newCountry.setGnp(BigDecimal.valueOf(0));
        newCountry.setGNPOld(BigDecimal.valueOf(0));
        newCountry.setLocalName("David");
        newCountry.setGovernmentForm("What?");
        newCountry.setHeadOfState("John");
        newCountry.setCapital(9000);
        newCountry.setCode2("A2");

        Assertions.assertFalse(worldService.updateCountryById("USA", newCountry));
    }

    @Test
    public void testUpdateCountryWithNullGovernmentForm() {
        CountryEntity newCountry = new CountryEntity();
        newCountry.setCode("A2U");
        newCountry.setName("TestCountry");
        newCountry.setPopulation(0);
        newCountry.setSurfaceArea(BigDecimal.valueOf(0));
        newCountry.setIndepYear((short) 0);
        newCountry.setLifeExpectancy(BigDecimal.valueOf(0));
        newCountry.setGnp(BigDecimal.valueOf(0));
        newCountry.setGNPOld(BigDecimal.valueOf(0));
        newCountry.setLocalName("David");
        newCountry.setGovernmentForm(null);  // Invalid
        newCountry.setHeadOfState("John");
        newCountry.setCapital(9000);
        newCountry.setCode2("A2");

        Assertions.assertFalse(worldService.updateCountryById("USA", newCountry));
    }

    // Language
    @Test
    public void testUpdateValidCountryLanguageById() {
        CountryLanguageIdEntity id = new CountryLanguageIdEntity();
        id.setCountryCode("USA");
        id.setLanguage("English");

        CountryLanguageEntity newCountryLanguage = new CountryLanguageEntity();
        newCountryLanguage.setIsOfficial("T");
        newCountryLanguage.setPercentage(BigDecimal.valueOf(50.f));

        CountryLanguageEntity existingLanguage = new CountryLanguageEntity();
        existingLanguage.setId(id);

        when(countryLanguageRepository.findById(id)).thenReturn(Optional.of(existingLanguage));
        when(countryLanguageRepository.save(any(CountryLanguageEntity.class))).thenReturn(existingLanguage);

        Assertions.assertTrue(worldService.updateLanguageById(id, newCountryLanguage));
    }

    @Test
    public void testUpdateInvalidCountryLanguageById() {
        CountryLanguageIdEntity id = new CountryLanguageIdEntity();
        id.setCountryCode("ZZZ");
        id.setLanguage("Nonexistent");

        CountryLanguageEntity newCountryLanguage = new CountryLanguageEntity();
        newCountryLanguage.setIsOfficial("T");
        newCountryLanguage.setPercentage(BigDecimal.valueOf(95.5));

        when(countryLanguageRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertFalse(worldService.updateLanguageById(id, newCountryLanguage));
    }

    @Test
    public void testUpdateLanguageWithNegativePercentage() {
        CountryLanguageIdEntity id = new CountryLanguageIdEntity();
        id.setCountryCode("USA");
        id.setLanguage("English");

        CountryLanguageEntity newCountryLanguage = new CountryLanguageEntity();
        newCountryLanguage.setIsOfficial("T");
        newCountryLanguage.setPercentage(BigDecimal.valueOf(-10));  // Invalid

        CountryLanguageEntity existingLanguage = new CountryLanguageEntity();
        existingLanguage.setId(id);

        when(countryLanguageRepository.findById(id)).thenReturn(Optional.of(existingLanguage));

        Assertions.assertFalse(worldService.updateLanguageById(id, newCountryLanguage));
    }

    @Test
    public void testUpdateLanguageWithPercentageOver100() {
        CountryLanguageIdEntity id = new CountryLanguageIdEntity();
        id.setCountryCode("USA");
        id.setLanguage("English");

        CountryLanguageEntity newCountryLanguage = new CountryLanguageEntity();
        newCountryLanguage.setIsOfficial("T");
        newCountryLanguage.setPercentage(BigDecimal.valueOf(110));  // Invalid

        CountryLanguageEntity existingLanguage = new CountryLanguageEntity();
        existingLanguage.setId(id);

        when(countryLanguageRepository.findById(id)).thenReturn(Optional.of(existingLanguage));

        Assertions.assertFalse(worldService.updateLanguageById(id, newCountryLanguage));
    }
}
