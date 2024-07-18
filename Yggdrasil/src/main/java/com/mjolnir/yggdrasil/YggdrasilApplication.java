package com.mjolnir.yggdrasil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YggdrasilApplication {
    public static void main(String[] args) {
        SpringApplication.run(YggdrasilApplication.class, args);
    }

    // test

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
