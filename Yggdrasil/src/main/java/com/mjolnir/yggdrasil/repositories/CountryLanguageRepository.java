package com.mjolnir.yggdrasil.repositories;

import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryLanguageRepository extends JpaRepository<CountryLanguageEntity, String> {
}
