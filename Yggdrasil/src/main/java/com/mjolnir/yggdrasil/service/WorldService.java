package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageIdEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import static com.mjolnir.yggdrasil.utilities.Regex.CONTINENT_REGEX;
import static com.mjolnir.yggdrasil.utilities.Regex.REGION_REGEX;

@Service
public class WorldService {
    private static final Logger logger = Logger.getLogger(WorldService.class.getName());
    private final CityRepository cityRepository;
    private final CountryLanguageRepository countryLanguageRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryLanguageRepository countryLanguageRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryLanguageRepository = countryLanguageRepository;
        this.countryRepository = countryRepository;
    }

    public void createNewCity(String countryCode, String cityName, String cityDistrict, Integer population, boolean isCapital) {
        Optional<CountryEntity> countryEntityOptional = countryRepository.findById(countryCode);
        if (countryEntityOptional.isPresent()
                && cityName != null
                && cityDistrict != null
                && population > 0) {

            CountryEntity countryEntity = countryEntityOptional.get();

            CityEntity cityEntity = new CityEntity();
            cityEntity.setName(cityName);
            cityEntity.setCountryCode(countryEntity);
            cityEntity.setDistrict(cityDistrict);
            cityEntity.setPopulation(population);
            cityRepository.save(cityEntity);

            //What if there is already a capital?
            if (isCapital) {
                countryEntity.setCapital(cityEntity.getId());
                countryRepository.save(countryEntity);
                logger.info("Capital city " + cityName + " added successfully for country " + countryEntity.getName() + ".");



            } else {
                logger.info("City " + cityName + " added successfully for country " + countryEntity.getName() + ".");
            }
        } else {
            if (countryEntityOptional.isEmpty()) {
                logger.warning("Country with code " + countryCode + " does not exist.");
            }
            if (cityName == null) {
                logger.warning("City name cannot be null.");
            }
            if (cityDistrict == null) {
                logger.warning("City district cannot be null.");
            }
            if (population < 0) {
                logger.warning("Population must be greater than 0.");
            }
        }
    }

    public void createNewCountry(String countryCode, String countryName, String continent, String region, BigDecimal surfaceArea, Short independenceYear, Integer population, BigDecimal lifeExpectancy, BigDecimal GNP, BigDecimal GNPOld, String localName, String governmentForm, String headOfState, String countryCode2, boolean hasACapital) {

        boolean countryCodeExists = countryRepository.existsById(countryCode);
        boolean countryCode2Exists = countryRepository.existsByCode2(countryCode2);

        if (!countryCodeExists && !countryCode2Exists
                && (countryName != null && countryName.length() < 52)
                && continent.matches(CONTINENT_REGEX)
                && (region.matches(REGION_REGEX) && region.length() < 26)
                && surfaceArea.compareTo(BigDecimal.ZERO) > 0
                && (independenceYear == null || independenceYear > 0)
                && (population == null || population > 0)
                && (lifeExpectancy == null || lifeExpectancy.compareTo(BigDecimal.ZERO) > 0)
                && (GNP == null || GNP.compareTo(BigDecimal.ZERO) > 0)
                && (GNPOld == null || GNPOld.compareTo(BigDecimal.ZERO) > 0)
                && (localName != null && localName.length() < 45)
                && governmentForm != null
                && (headOfState == null || !headOfState.isEmpty())
                && (countryCode2 != null && countryCode2.length() == 2)
                && (countryCode.length() == 3)) {

            CountryEntity newCountry = new CountryEntity();
            newCountry.setCode(countryCode);
            newCountry.setName(countryName);
            newCountry.setContinent(continent);
            newCountry.setRegion(region);
            newCountry.setSurfaceArea(surfaceArea);
            newCountry.setIndepYear(independenceYear);
            newCountry.setPopulation(population);
            newCountry.setLifeExpectancy(lifeExpectancy);
            newCountry.setGnp(GNP);
            newCountry.setGNPOld(GNPOld);
            newCountry.setLocalName(localName);
            newCountry.setGovernmentForm(governmentForm);
            newCountry.setHeadOfState(headOfState);
            newCountry.setCode2(countryCode2);

            countryRepository.save(newCountry);

            logger.info(countryName + " has been added successfully with country code " + countryCode + ".");

            String cityDistrict = "";
            String cityName = "";
            Integer cityPopulation = 1000;
            if (hasACapital) {
                createNewCity(countryCode, cityName, cityDistrict, cityPopulation, true);
            }
        } else {
            if (countryCodeExists) {
                System.out.println("Country with code '" + countryCode + "' already exists.");
            }
            if (countryCode2Exists) {
                System.out.println("Country with code2 '" + countryCode2 + "' already exists.");
            }
            if (countryName == null || countryName.length() >= 52) {
                System.out.println("Country name is null or exceeds 52 characters.");
            }
            if (!continent.matches(CONTINENT_REGEX)) {
                System.out.println("Continent '" + continent + "' does not match valid continents.");
            }
            if (!(region.matches(REGION_REGEX) && region.length() < 26)) {
                System.out.println("Region '" + region + "' does not match valid regions or exceeds 26 characters.");
            }
            if (!(surfaceArea.compareTo(BigDecimal.ZERO) > 0)) {
                System.out.println("Surface area should be greater than zero.");
            }
            if (!(independenceYear == null || independenceYear > 0)) {
                System.out.println("Independence year should be null or greater than zero.");
            }
            if (!(population == null || population > 0)) {
                System.out.println("Population should be null or greater than zero.");
            }
            if (!(lifeExpectancy == null || lifeExpectancy.compareTo(BigDecimal.ZERO) > 0)) {
                System.out.println("Life expectancy should be null or greater than zero.");
            }
            if (!(GNP == null || GNP.compareTo(BigDecimal.ZERO) > 0)) {
                System.out.println("GNP should be null or greater than zero.");
            }
            if (!(GNPOld == null || GNPOld.compareTo(BigDecimal.ZERO) > 0)) {
                System.out.println("GNPOld should be null or greater than zero.");
            }
            if (localName == null || localName.length() >= 45) {
                System.out.println("Local name is null or exceeds 45 characters.");
            }
            if (governmentForm == null) {
                System.out.println("Government form is null.");
            }
            if (!(headOfState == null || !headOfState.isEmpty())) {
                System.out.println("Head of state is null or empty.");
            }
            if (!(countryCode2 != null && countryCode2.length() == 2)) {
                System.out.println("Country code2 should not be null and must be exactly 2 characters.");
            }
            if (!(countryCode.length() == 3)) {
                System.out.println("Country code must be exactly 3 characters.");
            }
        }
    }

    public void createNewCountryLanguage(String countryCode, String language, String isOfficial, BigDecimal percentageSpoken) {
        // Finds country on the country table by getting it via the countryCode parameter.
        Optional<CountryEntity> countryEntityOptional = countryRepository.findById(countryCode);

        //checks if country exists, and correct parameters
        if (countryEntityOptional.isPresent()
                && language != null
                && (Objects.equals(isOfficial, "F") || Objects.equals(isOfficial, "T"))
                && percentageSpoken.compareTo(BigDecimal.ZERO) > 0
                && percentageSpoken.compareTo(BigDecimal.valueOf(100)) <= 0) {

            //Get countryEntity as an object (the whole row)
            CountryEntity countryEntity = countryEntityOptional.get();

            //Make new row in country language and country languageid tables
            CountryLanguageIdEntity countryLanguageIdEntity = new CountryLanguageIdEntity();
            CountryLanguageEntity countryLanguageEntity = new CountryLanguageEntity();


            countryLanguageEntity.setCountryCode(countryEntity);
            countryLanguageEntity.setLanguage(language);
            countryLanguageEntity.setIsOfficial(isOfficial);
            countryLanguageEntity.setPercentage(percentageSpoken);


            countryLanguageRepository.save(countryLanguageEntity);
            logger.info("Language '" + language + "' with country code " + countryCode + " added successfully.");

        } else {
            if (countryEntityOptional.isEmpty()) {
                logger.warning("Country with code " + countryCode + " does not exist.");
            }
            if (language == null) {
                logger.warning("Language cannot be null.");
            }
            if (percentageSpoken.compareTo(BigDecimal.ZERO) > 0) {
                logger.warning("Percentage of spoken should be greater than zero.");
            }
            if (percentageSpoken.compareTo(BigDecimal.valueOf(100)) <= 0) {
                logger.warning("Percentage of spoken should be less than 100");
            }
            if (isOfficial == null) {
                logger.warning("isOfficial cannot be null.");
            }
        }
    }



}
