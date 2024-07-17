package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.service.WorldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GetTop5DistrictsTests {
    @Autowired
    WorldService worldService;

    @Test
    public void testFindSmallest5Districts() {
        Assertions.assertEquals("[West Island->167, Fakaofo->300, Home Island->503, Wallis->1137, Länsimaa->1438]", worldService.findFiveSmallestDistricts().toString());
    }

    @Test
    public void testFindLargest5Districts() {
        Assertions.assertEquals("[São Paulo->26316966, Maharashtra->23659433, England->19978543, Punjab->19708438, California->16716706]", worldService.findFiveLargestDistricts().toString());
    }
}
