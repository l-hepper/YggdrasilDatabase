package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import com.mjolnir.yggdrasil.utilities.Regex;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UtilityTests {

    private static final Pattern CONTINENT_PATTERN = Pattern.compile(Regex.CONTINENT_REGEX);
    private static final Pattern REGION_PATTERN = Pattern.compile(Regex.REGION_REGEX);

    @ParameterizedTest
    @ValueSource(strings = {
            "North America",
            "South America",
            "Asia",
            "Africa",
            "Europe",
            "Oceania",
            "Antarctica"
    })
    public void testContinentRegexMatches(String input) {
        assertTrue(CONTINENT_PATTERN.matcher(input).matches());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "North AmericaX",
            "South",
            "Asiaa",
            "afric",
            "European",
            "Oce",
            "Antarctic"
    })
    public void testContinentRegexNonMatches(String input) {
        assertFalse(CONTINENT_PATTERN.matcher(input).matches());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Western Europe",
            "Western Africa",
            "Southern Europe",
            "Southern and Central Asia",
            "Southern Africa",
            "Southeast Asia",
            "South America",
            "Polynesia",
            "Northern Africa",
            "North America",
            "Nordic Countries",
            "Middle East",
            "Micronesia/Caribbean",
            "Micronesia",
            "Melanesia",
            "Eastern Europe",
            "Eastern Asia",
            "Eastern Africa",
            "Central America",
            "Central Africa",
            "Caribbean",
            "British Islands",
            "Australia and New Zealand",
            "Antarctica"
    })
    public void testRegionRegexMatches(String input) {
        assertTrue(REGION_PATTERN.matcher(input).matches());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Western Europ",
            "Western Afric",
            "Southern Europ",
            "Southern and Central Asi",
            "Southern Afric",
            "Southeast Asi",
            "South Americ",
            "Polynesi",
            "Northern Afric",
            "North Americ",
            "Nordic Countrie",
            "Middle Eas",
            "Micronesia/Caribbea",
            "Micronesiaa",
            "Melanesi",
            "Eastern Europ",
            "Eastern Asi",
            "Eastern Afric",
            "Central Americ",
            "Central Afric",
            "Caribbea",
            "British Island",
            "Australia and New Zealan",
            "Antarctic"
    })
    public void testRegionRegexNonMatches(String input) {
        assertFalse(REGION_PATTERN.matcher(input).matches());
    }
}


