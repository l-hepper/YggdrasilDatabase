package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
public class GetLanguagesByCountryCodeTest {
    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private WorldService worldService;

    @Test
    public void testGetLanguagesByCountryCodeValidCode() {
        CountryEntity mockCountry = new CountryEntity();
        CountryLanguageEntity lang1 = new CountryLanguageEntity();
        CountryLanguageEntity lang2 = new CountryLanguageEntity();
        mockCountry.setCountrylanguages(Set.of(lang1, lang2));

        when(countryRepository.findById("USA")).thenReturn(Optional.of(mockCountry));

        List<CountryLanguageEntity> languages = worldService.getLanguagesByCountryCode("USA");

        Assertions.assertEquals(2, languages.size());
    }

    @Test
    public void testGetLanguagesByCountryCodeInvalidCode() {
        when(countryRepository.findById("XYZ")).thenReturn(Optional.empty());

        List<CountryLanguageEntity> languages = worldService.getLanguagesByCountryCode("XYZ");

        Assertions.assertTrue(languages.isEmpty());
    }

    @Test
    public void testGetLanguagesByCountryCodeEmptyLanguages() {
        CountryEntity mockCountry = new CountryEntity();
        mockCountry.setCountrylanguages(Collections.emptySet());

        when(countryRepository.findById("USA")).thenReturn(Optional.of(mockCountry));

        List<CountryLanguageEntity> languages = worldService.getLanguagesByCountryCode("USA");

        Assertions.assertTrue(languages.isEmpty());
    }
}