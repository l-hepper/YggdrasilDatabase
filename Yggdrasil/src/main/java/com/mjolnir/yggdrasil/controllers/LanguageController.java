package com.mjolnir.yggdrasil.controllers;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageIdEntity;
import com.mjolnir.yggdrasil.exceptions.InvalidBodyException;
import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/Yggdrasil/languages")
public class LanguageController {
    private final WorldService worldService;

    public LanguageController(WorldService worldService) {
        this.worldService = worldService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<CountryLanguageEntity>> createLanguage(@RequestBody CountryLanguageEntity language, HttpServletRequest request) {
        try {
            worldService.createNewCountryLanguage(language.getId().getCountryCode(), language.getLanguage(), language.getIsOfficial(), language.getPercentage());
        } catch (IllegalArgumentException e) {
            throw new InvalidBodyException("Language not created: " + e.getMessage());
        }

        Optional<CountryLanguageEntity> lang = worldService.getLanguageById(language.getId().getCountryCode(), language.getLanguage());

        if (lang.isEmpty())
            throw new ResourceNotFoundException("Language not created");
        else{
            URI location = URI.create(request.getRequestURL().toString() + '/' + lang.get().getCountryCode().getCode() + '/' + lang.get().getLanguage());
            return ResponseEntity.created(location).body(EntityModel.of(lang.get()));
        }
    }


    @GetMapping("/{countryCode}/{language}")
    public ResponseEntity<EntityModel<CountryLanguageEntity>> getLanguageById(@PathVariable String countryCode, @PathVariable String language) {
        EntityModel<CountryLanguageEntity> languageEntity = worldService.getLanguageById(countryCode, language)
                .map(lang -> {
                    Link countryLink = Link.of("/countries/" + countryCode).withRel(countryCode);
                    Link selfLink = WebMvcLinkBuilder.linkTo(
                            methodOn(LanguageController.class).getLanguageById(lang.getCountryCode().getCode(), lang.getLanguage())).withSelfRel();
                    Link relLink = WebMvcLinkBuilder.linkTo(
                            methodOn(LanguageController.class).getAllLanguages()).withRel("languages");
                    return EntityModel.of(lang, selfLink, relLink, countryLink);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Language not found"));

        return ResponseEntity.ok(languageEntity);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CountryLanguageEntity>>> getAllLanguages() {
        List<CountryLanguageEntity> languages = worldService.getAllLanguages();
        List<EntityModel<CountryLanguageEntity>> resources = languages.stream()
                .map(language -> {
                    String countryCode = language.getCountryCode().getCode();
                    Link countryLink = Link.of("/countries/" + countryCode).withRel(countryCode);
                    Link selfLink = WebMvcLinkBuilder.linkTo(
                            methodOn(LanguageController.class).getLanguageById(language.getCountryCode().getCode(), language.getLanguage())).withSelfRel();
                    Link relLink = WebMvcLinkBuilder.linkTo(
                            methodOn(LanguageController.class).getAllLanguages()).withRel("languages");
                    return EntityModel.of(language, selfLink, relLink, countryLink);
                })
                .toList();

        return ResponseEntity.ok(CollectionModel.of(
                resources,
                WebMvcLinkBuilder.linkTo(methodOn(LanguageController.class).getAllLanguages()).withSelfRel()));
    }

    @PutMapping("/{countryCode}/{language}")
    public ResponseEntity<EntityModel<CountryLanguageEntity>> updateLanguage(@PathVariable String countryCode, @PathVariable String language, @RequestBody CountryLanguageEntity updatedLanguage) {
        CountryLanguageIdEntity primaryKey = new CountryLanguageIdEntity();
        primaryKey.setCountryCode(countryCode);
        primaryKey.setLanguage(language);
        boolean wasUpdateSuccessful = worldService.updateLanguageById(primaryKey, updatedLanguage);

        if (wasUpdateSuccessful) {

            Optional<CountryLanguageEntity> lang = worldService.getLanguageById(countryCode, language);

            if (lang.isEmpty())
                throw new InvalidBodyException("Language: " + countryCode + language + " not updated");

            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Language not updated");
        }
    }

    @DeleteMapping("/{countryCode}/{language}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String countryCode, @PathVariable String language) {
        boolean wasDeleteSuccessful = worldService.deleteLanguageByCountryCodeAndLanguage(countryCode, language);

        if (wasDeleteSuccessful) {
            return ResponseEntity.noContent().build();
        }

        throw new InvalidBodyException("Language: " + countryCode + language + " not deleted");
    }
}
