package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
public class DeleteCountryByCodeTests {

    @Autowired
    WorldService worldService;

    @Autowired
    CountryRepository countryRepository;

    @Test
    void testDeleteCountryByCode() {
        worldService.deleteCountryByCode("AUS");
        boolean exists = countryRepository.existsById("AUS");
        Assertions.assertFalse(exists);
    }

    @Test
    void testDeleteCountryByCodeReturnsFalseForNonExistentCountry() {
        worldService.deleteCountryByCode("XXX");
        boolean exists = countryRepository.existsById("XXX");
        Assertions.assertFalse(exists);
    }

}
