package com.mjolnir.yggdrasil.repositories;

import com.mjolnir.yggdrasil.entities.MjolnirApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MjolnirKeyRepository extends JpaRepository<MjolnirApiKey, Long> {
    MjolnirApiKey findByApiKey(String apiKey);
}
