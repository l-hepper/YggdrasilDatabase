package com.mjolnir.yggdrasil.controllers;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<EntityModel<CountryEntity>> getCountry(@PathVariable String id) {
        Optional<CountryEntity> country = countryRepository.findById(id);
        if (!country.isPresent()) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        EntityModel<CountryEntity> resource = EntityModel.of(country.get());
        resource.add(linkTo(methodOn(CountryController.class).getCountry(id)).withSelfRel());
        resource.add(linkTo(methodOn(CountryController.class).getCountries()).withRel("all-countries"));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CountryEntity> createCountry(@RequestBody CountryEntity country) {
        CountryEntity countryEntity = null;
        try {
            countryEntity = countryRepository.save(country);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(countryEntity);
    }

    @PutMapping
    public ResponseEntity<CountryEntity> updateCountry(@RequestBody CountryEntity country) {
        CountryEntity countryEntity = countryRepository.save(country);
        return ResponseEntity.ok(countryEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCountry(@PathVariable String id) {
        Optional<CountryEntity> country = countryRepository.findById(id);
        if (!country.isPresent()) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        countryRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CountryEntity> partialUpdateCountry(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Optional<CountryEntity> optionalCountry = countryRepository.findById(id);

        if (!optionalCountry.isPresent()) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        CountryEntity country = optionalCountry.get();

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "name" -> country.setName((String) value);
                case "continent" -> country.setContinent((String) value);
                case "region" -> country.setRegion((String) value);
                case "surfaceArea" -> country.setSurfaceArea(new BigDecimal(value.toString()));
                case "indepYear" -> country.setIndepYear(Short.parseShort(value.toString()));
                case "population" -> country.setPopulation(Integer.parseInt(value.toString()));
                case "lifeExpectancy" -> country.setLifeExpectancy(new BigDecimal(value.toString()));
                case "gnp" -> country.setGnp(new BigDecimal(value.toString()));
                case "localName" -> country.setLocalName((String) value);
                case "governmentForm" -> country.setGovernmentForm((String) value);
                case "headOfState" -> country.setHeadOfState((String) value);
                case "capital" -> country.setCapital(Integer.parseInt(value.toString()));
                case "code2" -> country.setCode2((String) value);
                case "gnpold" -> country.setGNPOld(new BigDecimal(value.toString()));
                default -> throw new ResourceNotFoundException("The field '" + key + "' is not valid.");
            }
        }

        CountryEntity updatedCountry = countryRepository.save(country);
        return ResponseEntity.ok(updatedCountry);
    }
}
