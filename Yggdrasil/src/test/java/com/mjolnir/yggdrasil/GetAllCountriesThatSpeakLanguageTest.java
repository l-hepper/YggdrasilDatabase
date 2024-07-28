//package com.mjolnir.yggdrasil;
//
//import com.mjolnir.yggdrasil.entities.CountryEntity;
//import com.mjolnir.yggdrasil.service.WorldService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//@Transactional
//@SpringBootTest
//public class GetAllCountriesThatSpeakLanguageTest {
//    @Autowired
//    private WorldService worldService;
//
//    @Test
//    void testGetAllCountriesThatSpeakLanguageReturnsEmpty() {
//        List<CountryEntity> countriesThatSpeakLanguage = worldService.getAllCountriesThatSpeakLanguage("asdasdasd");
//        Assertions.assertTrue(countriesThatSpeakLanguage.isEmpty());
//    }
//
//    @Test
//    void testGetAllCountriesThatSpeakLanguageIsSize5() {
//        List<CountryEntity> countriesThatSpeakLanguage = worldService.getAllCountriesThatSpeakLanguage("Dutch");
//
//        Assertions.assertEquals(5, countriesThatSpeakLanguage.size());
//    }
//
////    @Test
////    void testGetCountriesWithoutAHeadOfStateReturnsNoCountriesIfNoneArePresent() {
////        List<CountryEntity> countriesWithoutAHeadOfState = worldService.getAllCountriesWithoutAHeadOfState();
////
////        for (CountryEntity country : countriesWithoutAHeadOfState) {
////            worldService.deleteCountryByCode(country.getCode());
////        }
////
////        List<CountryEntity> emptyList = worldService.getAllCountriesWithoutAHeadOfState();
////        Assertions.assertTrue(emptyList.isEmpty());
////}
//}