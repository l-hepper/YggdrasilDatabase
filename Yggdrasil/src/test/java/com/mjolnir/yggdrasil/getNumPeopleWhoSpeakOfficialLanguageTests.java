package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.NoSuchElementException;

@Transactional
@SpringBootTest
public class getNumPeopleWhoSpeakOfficialLanguageTests {

    @Autowired
    WorldService worldService;

    @Test
    public void testNumberOfPeopleWhoSpeakEnglishInAustralia() {
        int peopleInAusWhoSpeakEnglish = worldService.getHowManyPeopleSpeakOfficialLanguageIn("AUS");
        Assertions.assertEquals(15_335_432, peopleInAusWhoSpeakEnglish);
    }

    @Test
    public void testNumberOfPeopleWhoSpeakEnglishInUSA() {
        int peopleInAusWhoSpeakEnglish = worldService.getHowManyPeopleSpeakOfficialLanguageIn("USA");
        Assertions.assertEquals(239_943_734, peopleInAusWhoSpeakEnglish);
    }

    @Test
    public void testNumberOfPeopleWhoSpeakEnglishInFrance() {
        int peopleInFranceWhoSpeakFrench = worldService.getHowManyPeopleSpeakOfficialLanguageIn("FRA");
        Assertions.assertEquals(55_435_255, peopleInFranceWhoSpeakFrench);
    }

    @Test
    public void testCountryWithNoOfficialLanguageReturns0() {
        int peopleWhoSpeakOfficialLanguageInAGO = worldService.getHowManyPeopleSpeakOfficialLanguageIn("AGO");
        Assertions.assertEquals(0, peopleWhoSpeakOfficialLanguageInAGO);
    }
}
