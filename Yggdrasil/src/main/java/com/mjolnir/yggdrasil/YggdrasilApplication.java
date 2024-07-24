package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.entities.MjolnirApiKey;
import com.mjolnir.yggdrasil.entities.User;
import com.mjolnir.yggdrasil.repositories.MjolnirKeyRepository;
import com.mjolnir.yggdrasil.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class YggdrasilApplication {
    public static void main(String[] args) {
        SpringApplication.run(YggdrasilApplication.class, args);
    }

    // test
//    @Bean
//    CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            Set<String> adminRoles = new HashSet<>();
//            adminRoles.add("ROLE_ADMIN");
//            userRepository.save(new User("admin", passwordEncoder.encode("password"), adminRoles));
//
//            Set<String> userRoles = new HashSet<>();
//            userRoles.add("ROLE_USER");
//            userRepository.save(new User("user", passwordEncoder.encode("password"), userRoles));
//        };
//    }

//    @Bean
//    CommandLineRunner loadData(MjolnirKeyRepository apiKeyRepository) {
//        return args -> {
//            MjolnirApiKey readOnlyKey = new MjolnirApiKey();
//            readOnlyKey.setApiKey("read-only-key");
//            readOnlyKey.setRole("READ_ONLY");
//            apiKeyRepository.save(readOnlyKey);
//
//            MjolnirApiKey devKey = new MjolnirApiKey();
//            devKey.setApiKey("dev-full-access-key");
//            devKey.setRole("FULL_ACCESS");
//            apiKeyRepository.save(devKey);
//
//            MjolnirApiKey lewisKey = new MjolnirApiKey();
//            lewisKey.setApiKey("lewis-full-access-key");
//            lewisKey.setRole("FULL_ACCESS");
//            apiKeyRepository.save(lewisKey);
//
//            MjolnirApiKey liamKey = new MjolnirApiKey();
//            liamKey.setApiKey("liam-full-access-key");
//            liamKey.setRole("FULL_ACCESS");
//            apiKeyRepository.save(liamKey);
//        };
//    }

//    @Bean
//    public static CommandLineRunner commandLineRunner(WorldService worldService) {
//        return args -> {
//            worldService.getAllCountriesThatSpeakLanguage("English").forEach(country -> System.out.println(country.getName()));
//        };
//    }
}
