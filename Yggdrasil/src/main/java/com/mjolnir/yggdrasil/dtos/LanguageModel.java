package com.mjolnir.yggdrasil.dtos;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public class LanguageModel {
    private final String language;
    private final List<EntityModel<CountryEntity>> countries;

    public LanguageModel(String language, List<EntityModel<CountryEntity>> countries) {
        this.language = language;
        this.countries = countries;
    }

    public String getLanguage() {
        return language;
    }

    public List<EntityModel<CountryEntity>> getCountries() {
        return countries;
    }
}
