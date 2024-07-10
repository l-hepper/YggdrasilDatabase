package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
class YggdrasilApplicationTests {

    @Test
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
        System.out.println(worldService.createNewCity("ZWE", "Lewis's City", "Lewis's District", 1234567890));
    }

    @Test
    @DisplayName("Check createNewCity service method doesnt add new city with false country code")
    void checkCreateNewCityServiceMethodDoesntAddNewCityWithFalseCountryCode() {
        System.out.println(worldService.createNewCity("ZZZ", "Lewis's City", "Lewis's District", 1234567890));
    }

}
