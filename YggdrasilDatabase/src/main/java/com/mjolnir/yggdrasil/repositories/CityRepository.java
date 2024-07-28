package com.mjolnir.yggdrasil.repositories;

import com.mjolnir.yggdrasil.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer> {
    Optional<CityEntity> findByName(String name);
}
