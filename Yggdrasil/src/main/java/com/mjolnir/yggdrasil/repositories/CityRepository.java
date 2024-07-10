package com.mjolnir.yggdrasil.repositories;

import com.mjolnir.yggdrasil.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {
}
