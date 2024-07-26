package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.MjolnirApiKey;
import com.mjolnir.yggdrasil.entities.SecurityUser;
import com.mjolnir.yggdrasil.entities.User;
import com.mjolnir.yggdrasil.repositories.MjolnirKeyRepository;
import com.mjolnir.yggdrasil.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MjolnirApiService implements UserDetailsService {
    private final MjolnirKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    @Autowired
    public MjolnirApiService(MjolnirKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }

    @Deprecated
    public Optional<String> generateApiKey(String role) {
        if (!role.equals("FULL_ACCESS") && !role.equals("READ_ONLY"))
            return Optional.empty();

        MjolnirApiKey apiKey = new MjolnirApiKey();
        apiKey.setApiKey(UUID.randomUUID().toString());
        apiKey.setRole(role);
        apiKeyRepository.save(apiKey);

        return Optional.of(apiKey.getApiKey());
    }

    @Deprecated
    public String getRoleFromApiKey(String apiKey) {
        MjolnirApiKey key = apiKeyRepository.findByApiKey(apiKey);

        if (key == null)
            return null;

        return key.getRole();
    }

    @Deprecated
    public boolean isApiKeyValid(String apiKey) {
        MjolnirApiKey key = apiKeyRepository.findByApiKey(apiKey);
        return key != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        return user
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found"));
    }
}
