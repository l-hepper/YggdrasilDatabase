package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
public class percentageOfPopulationInLargestCityTests {

    @Autowired
    WorldService worldService;

    @Test
    public void testPercentageOfPopulationInLondon() {
        double percentage = worldService.whatPercentageOfPopulationLivesInLargestCityIn("GBR");
        Assertions.assertEquals(12.22, percentage, 0.1);
    }

    @Test
    public void testPercentageOfPopulationInJapan() {
        double percentage = worldService.whatPercentageOfPopulationLivesInLargestCityIn("JPN");
        Assertions.assertEquals(6.30, percentage, 0.1);
    }

    @Test
    public void testPercentageOfPopulationInNonExistentCountry() {
        double percentage = worldService.whatPercentageOfPopulationLivesInLargestCityIn("XXX");
        Assertions.assertEquals(0, percentage, 0);
    }


}
