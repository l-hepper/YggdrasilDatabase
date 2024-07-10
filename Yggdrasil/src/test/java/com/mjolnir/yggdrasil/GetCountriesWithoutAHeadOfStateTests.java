package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageIdEntity;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Transactional
@SpringBootTest
public class GetCountriesWithoutAHeadOfStateTests {

    @Autowired
    private WorldService worldService;

    @Test
    void testGetCountriesWithoutAHeadOfStateReturnsAtLeast1Country() {
        List<CountryEntity> countriesWithoutAHeadOfState = worldService.getAllCountriesWithoutAHeadOfState();
        Assertions.assertFalse(countriesWithoutAHeadOfState.isEmpty());
    }

    @Test
    void testGetCountriesWithoutAHeadOfStateReturnsCountriesWithoutHeadsOfState() {
        List<CountryEntity> countriesWithoutAHeadOfState = worldService.getAllCountriesWithoutAHeadOfState();

        boolean flag = false;
        for (CountryEntity country : countriesWithoutAHeadOfState) {
            if (country.getHeadOfState() != null && !country.getHeadOfState().isBlank()) {
                flag = true;
            }
        }
        Assertions.assertFalse(flag);
    }

    @Test
    void testGetCountriesWithoutAHeadOfStateReturnsNoCountriesIfNoneArePresent() {
        List<CountryEntity> countriesWithoutAHeadOfState = worldService.getAllCountriesWithoutAHeadOfState();

        for (CountryEntity country : countriesWithoutAHeadOfState) {
            worldService.deleteCountryByCode(country.getCode());
        }

        List<CountryEntity> emptyList = worldService.getAllCountriesWithoutAHeadOfState();
        Assertions.assertTrue(emptyList.isEmpty());
    }
}
