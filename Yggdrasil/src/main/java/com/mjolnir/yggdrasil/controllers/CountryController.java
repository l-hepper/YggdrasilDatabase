package com.mjolnir.yggdrasil.controllers;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final WorldService worldService;
    private final CountryRepository countryRepository;

    @Autowired
    public CountryController(WorldService worldService, CountryRepository countryRepository) {
        this.worldService = worldService;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public List<CountryEntity> getCountries() {
        List<CountryEntity> countries = countryRepository.findAll();
        return countries;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryEntity> getCountries(@PathVariable String id) {
        Optional<CountryEntity> country = countryRepository.findById(id);
        if (!country.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(country.get());
    }

    @PostMapping
    public ResponseEntity<CountryEntity> createCountry(@RequestBody CountryEntity country) {
        CountryEntity countryEntity = countryRepository.save(country);
        return ResponseEntity.ok(countryEntity);
    }

    @PutMapping
    public ResponseEntity<CountryEntity> updateCountry(@RequestBody CountryEntity country) {
        CountryEntity countryEntity = countryRepository.save(country);
        return ResponseEntity.ok(countryEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCountry(@PathVariable String id) {
        countryRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
