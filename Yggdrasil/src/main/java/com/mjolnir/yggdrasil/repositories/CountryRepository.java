package com.mjolnir.yggdrasil.repositories;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, String> {
    boolean existsByCode2(String code2);

    Optional<CountryEntity> findCountryEntityByName(String name);
    List<CountryEntity> findByNameContaining(String name);
    List<CountryEntity> findCountryEntitiesByContinent(String continent);
    List<CountryEntity> findCountryEntitiesByRegion(String region);
    List<CountryEntity> findCountryEntitiesByPopulationBetween(Integer start, Integer end);
    List<CountryEntity> findCountryEntitiesByGovernmentForm(String governmentForm);
}
