package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class YggdrasilApplication {

    public static void main(String[] args) {
        SpringApplication.run(YggdrasilApplication.class, args);
    }

    // test
//    @Bean
//    public static CommandLineRunner commandLineRunner(WorldService worldService) {
//        return args -> {
//            worldService.getAllCountriesThatSpeakLanguage("English").forEach(country -> System.out.println(country.getName()));
//        };
//    }
}
