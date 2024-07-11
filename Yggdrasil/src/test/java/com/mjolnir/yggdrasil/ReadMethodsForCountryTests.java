package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class ReadMethodsForCountryTests {

    @Autowired
    WorldService worldService;

    @Autowired
    CountryRepository countryRepository;

    @Test
    public void testGetCountryByName() {
        Optional<CountryEntity> country = worldService.getCountryByName("United Kingdom");
        Assertions.assertEquals(country.get().getCode(), "GBR");
    }

    @Test
    public void testGetCountriesByNameLikeReturnsEverything() {
        List<CountryEntity> countries = worldService.getCountriesByNameLike("");
        Assertions.assertEquals(239, countries.size());
    }

    @Test
    public void testGetCountriesByNameLikeU() {
        List<CountryEntity> countries = worldService.getCountriesByNameLike("U");
        Assertions.assertEquals(78, countries.size());
    }

    @Test
    public void testGetCountriesByContinent() {
        List<CountryEntity> countries = worldService.getCountriesByContinent("North America");
        Assertions.assertEquals(37, countries.size());
    }

    @Test
    public void testGetCountriesByRegion() {
        List<CountryEntity> countries = worldService.getCountriesByRegion("Caribbean");
        Assertions.assertEquals(24, countries.size());
    }

    @Test
    public void testGetCountriesByPopulationBetween() {
        List<CountryEntity> countries = worldService.getCountriesByPopulationBetween(0, 50000);
        Assertions.assertEquals(34, countries.size());
    }
}
