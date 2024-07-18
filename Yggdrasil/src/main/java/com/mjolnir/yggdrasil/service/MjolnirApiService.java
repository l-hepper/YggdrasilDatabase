package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.MjolnirApiKey;
import com.mjolnir.yggdrasil.repositories.MjolnirKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MjolnirApiService {
    private final MjolnirKeyRepository apiKeyRepository;

    @Autowired
    public MjolnirApiService(MjolnirKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public Optional<String> generateApiKey(String role) {
        if (!role.equals("FULL_ACCESS") && !role.equals("READ_ONLY"))
            return Optional.empty();

        MjolnirApiKey apiKey = new MjolnirApiKey();
        apiKey.setApiKey(UUID.randomUUID().toString());
        apiKey.setRole(role);
        apiKeyRepository.save(apiKey);

        return Optional.of(apiKey.getApiKey());
    }

    public String getRoleFromApiKey(String apiKey) {
        MjolnirApiKey key = apiKeyRepository.findByApiKey(apiKey);

        if(key == null)
            return null;

        return apiKeyRepository.findByApiKey(apiKey).getRole();
    }
}
