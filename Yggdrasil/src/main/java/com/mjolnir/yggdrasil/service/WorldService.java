package com.mjolnir.yggdrasil.service;

import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.entities.CountryLanguageIdEntity;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import org.springframework.data.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.mjolnir.yggdrasil.utilities.Regex.CONTINENT_REGEX;
import static com.mjolnir.yggdrasil.utilities.Regex.REGION_REGEX;

@Service
public class WorldService {
    private static final Logger logger = Logger.getLogger(WorldService.class.getName());
    private final CityRepository cityRepository;
    private final CountryLanguageRepository countryLanguageRepository;
    private final CountryRepository countryRepository;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryLanguageRepository countryLanguageRepository, CountryRepository countryRepository, DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration) {
        this.cityRepository = cityRepository;
        this.countryLanguageRepository = countryLanguageRepository;
        this.countryRepository = countryRepository;
        this.dataSourceTransactionManagerAutoConfiguration = dataSourceTransactionManagerAutoConfiguration;
    }


    public void createNewCity(String countryCode, String cityName, String cityDistrict, Integer population, boolean isCapital) {
        Optional<CountryEntity> countryEntityOptional = countryRepository.findById(countryCode);
        if (countryEntityOptional.isPresent() && cityName != null && cityDistrict != null && population > 0) {

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

        if (!countryCodeExists && !countryCode2Exists && (countryName != null && countryName.length() < 52) && continent.matches(CONTINENT_REGEX) && (region.matches(REGION_REGEX) && region.length() < 26) && surfaceArea.compareTo(BigDecimal.ZERO) > 0 && (independenceYear == null || independenceYear > 0) && (population == null || population > 0) && (lifeExpectancy == null || lifeExpectancy.compareTo(BigDecimal.ZERO) > 0) && (GNP == null || GNP.compareTo(BigDecimal.ZERO) > 0) && (GNPOld == null || GNPOld.compareTo(BigDecimal.ZERO) > 0) && (localName != null && localName.length() < 45) && governmentForm != null && (headOfState == null || !headOfState.isEmpty()) && (countryCode2 != null && countryCode2.length() == 2) && (countryCode.length() == 3)) {

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
                logger.warning("Country with code '" + countryCode + "' already exists.");
            }
            if (countryCode2Exists) {
                logger.warning("Country with code2 '" + countryCode2 + "' already exists.");
            }
            if (countryName == null || countryName.length() >= 52) {
                logger.warning("Country name is null or exceeds 52 characters.");
            }
            if (!continent.matches(CONTINENT_REGEX)) {
                logger.warning("Continent '" + continent + "' does not match valid continents.");
            }
            if (!(region.matches(REGION_REGEX) && region.length() < 26)) {
                logger.warning("Region '" + region + "' does not match valid regions or exceeds 26 characters.");
            }
            if (!(surfaceArea.compareTo(BigDecimal.ZERO) > 0)) {
                logger.warning("Surface area should be greater than zero.");
            }
            if (!(independenceYear == null || independenceYear > 0)) {
                logger.warning("Independence year should be null or greater than zero.");
            }
            if (!(population == null || population > 0)) {
                logger.warning("Population should be null or greater than zero.");
            }
            if (!(lifeExpectancy == null || lifeExpectancy.compareTo(BigDecimal.ZERO) > 0)) {
                logger.warning("Life expectancy should be null or greater than zero.");
            }
            if (!(GNP == null || GNP.compareTo(BigDecimal.ZERO) > 0)) {
                logger.warning("GNP should be null or greater than zero.");
            }
            if (!(GNPOld == null || GNPOld.compareTo(BigDecimal.ZERO) > 0)) {
                logger.warning("GNPOld should be null or greater than zero.");
            }
            if (localName == null || localName.length() >= 45) {
                logger.warning("Local name is null or exceeds 45 characters.");
            }
            if (governmentForm == null) {
                logger.warning("Government form is null.");
            }
            if (!(headOfState == null || !headOfState.isEmpty())) {
                logger.warning("Head of state is null or empty.");
            }
            if (!(countryCode2 != null && countryCode2.length() == 2)) {
                logger.warning("Country code2 should not be null and must be exactly 2 characters.");
            }
            if (!(countryCode.length() == 3)) {
                logger.warning("Country code must be exactly 3 characters.");
            }
        }
    }

    public void createNewCountryLanguage(String countryCode, String language, String isOfficial, BigDecimal percentageSpoken) {
        Optional<CountryEntity> countryEntityOptional = countryRepository.findById(countryCode);

        if (countryEntityOptional.isPresent() && language != null && (Objects.equals(isOfficial, "F") || Objects.equals(isOfficial, "T")) && percentageSpoken.compareTo(BigDecimal.ZERO) > 0 && percentageSpoken.compareTo(BigDecimal.valueOf(100)) <= 0) {

            CountryEntity countryEntity = countryEntityOptional.get();

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

    public boolean deleteCountryByCode(String countryCode) {
        Optional<CountryEntity> country = countryRepository.findById(countryCode);

        if (country.isPresent()) {
            countryRepository.delete(country.get());
            logger.info("Country with code " + countryCode + ": " + country.get().getName() + " is successfully deleted.");
            return true;
        } else {
            logger.info("Country with code " + countryCode + " does not exist.");
            return false;
        }
    }

    public boolean deleteCityById(int cityID) {
        Optional<CityEntity> city = cityRepository.findById(cityID);

        if (city.isPresent()) {
            cityRepository.delete(city.get());
            logger.info("City with ID " + cityID + " is successfully deleted.");
            return true;
        } else {
            logger.info("City with ID " + cityID + " does not exist.");
            return false;
        }
    }

    public boolean deleteLanguageByCountryCodeAndLanguage(String countryCode, String language) {
        CountryLanguageIdEntity primaryKey = new CountryLanguageIdEntity();
        primaryKey.setCountryCode(countryCode);
        primaryKey.setLanguage(language);

        Optional<CountryLanguageEntity> countryLanguage = countryLanguageRepository.findById(primaryKey);
        if (countryLanguage.isPresent()) {
            countryLanguageRepository.delete(countryLanguage.get());
            logger.info("CountryLanguage with CountryCode: " + countryCode + " and Language: " + language + " is successfully deleted.");
            return true;
        } else {
            logger.info("CountryLanguage with CountryCode: " + countryCode + " or Language: " + language + " does not exist.");
            return false;
        }
    }

    public boolean deleteLanguageByLanguage(String languageToDelete) {
        List<CountryLanguageEntity> countryLanguages = countryLanguageRepository.findAll();

        for (CountryLanguageEntity language : countryLanguages) {
            if (language.getId().getLanguage().equals(languageToDelete)) {
                countryLanguageRepository.delete(language);
            }
        }

        List<CountryLanguageEntity> newCountryLanguages = countryLanguageRepository.findAll();
        if (newCountryLanguages.size() < countryLanguages.size()) {
            logger.info("All instances of Language:" + languageToDelete + " have been deleted.");
            return true;
        } else {
            logger.info("No instances of Language:" + languageToDelete + " found.");
            return false;
        }
    }

    public List<CountryEntity> getAllCountriesWithoutAHeadOfState() {
        List<CountryEntity> countriesWithoutAHeadOfState = new ArrayList<>();

        List<CountryEntity> countries = countryRepository.findAll();
        for (CountryEntity country : countries) {
            if (country.getHeadOfState() == null || country.getHeadOfState().isBlank()) {
                countriesWithoutAHeadOfState.add(country);
            }
        }

        logger.info("Returning list of size:" + countriesWithoutAHeadOfState.size() + " without a head of state.");
        return countriesWithoutAHeadOfState;
    }

    public int getHowManyPeopleSpeakOfficialLanguageIn(String countryCode) {
        Optional<CountryEntity> country = countryRepository.findById(countryCode);

        if (!country.isPresent()) {
            return 0;
        }

        int population = country.get().getPopulation();

        List<CountryLanguageEntity> officialLanguages = getOfficialLanguagesIn(countryCode);
        Optional<CountryLanguageEntity> mostSpoken = getMostSpokenLanguage(officialLanguages);
        if (mostSpoken.isPresent()) {
            int numberOfPeopleWhoSpeakMostPopularOfficialLanguage = (int) ((population / 100) * mostSpoken.get().getPercentage().doubleValue());
            logger.info("Most popular official language in " + country.get().getName() +
                    " is " + mostSpoken.get().getLanguage() +
                    " with " + numberOfPeopleWhoSpeakMostPopularOfficialLanguage +
                    " speakers.");
            return numberOfPeopleWhoSpeakMostPopularOfficialLanguage;
        } else {
            return 0;
        }
    }

    private List<CountryLanguageEntity> getOfficialLanguagesIn(String countryCode) {
        List<CountryLanguageEntity> languages = countryLanguageRepository.findAll();
        List<CountryLanguageEntity> officialLanguages = new ArrayList<>();
        for (CountryLanguageEntity language : languages) {
            if (language.getId().getCountryCode().equals(countryCode) && language.getIsOfficial().equals("T")) {
                officialLanguages.add(language);
            }
        }
        return officialLanguages;
    }

    private Optional<CountryLanguageEntity> getMostSpokenLanguage(List<CountryLanguageEntity> languages) {
        if (languages.isEmpty()) {
            return Optional.empty();
        }
        CountryLanguageEntity mostSpokenLanguage = languages.get(0);
        for (CountryLanguageEntity language : languages) {
            if (language.getPercentage().compareTo(mostSpokenLanguage.getPercentage()) > 0) {
                mostSpokenLanguage = language;
            }
        }
        return Optional.of(mostSpokenLanguage);
    }

    // Update
    public boolean updateCityById(Integer id, CityEntity city) {
        if (id == null || city == null || city.getPopulation() < 0) {
            return false;
        }

        Optional<CityEntity> toUpdate = cityRepository.findById(id);
        if (toUpdate.isPresent()) {
            CityEntity existingCity = toUpdate.get();
            existingCity.setName(city.getName());
            existingCity.setDistrict(city.getDistrict());
            existingCity.setPopulation(city.getPopulation());
            cityRepository.save(existingCity);
            return true;
        }
        return false;
    }

    public boolean updateCountryById(String id, CountryEntity country) {
        if (id == null || country == null || country.getPopulation() < 0 || country.getLifeExpectancy().compareTo(BigDecimal.ZERO) < 0 || country.getGnp().compareTo(BigDecimal.ZERO) < 0 || (country.getGNPOld() != null && country.getGNPOld().compareTo(BigDecimal.ZERO) < 0) || country.getGovernmentForm() == null) {
            return false;
        }

        Optional<CountryEntity> toUpdate = countryRepository.findById(id);
        if (toUpdate.isPresent()) {
            CountryEntity existingCountry = getCountryEntity(country, toUpdate);
            countryRepository.save(existingCountry);
            return true;
        }
        return false;
    }

    private static CountryEntity getCountryEntity(CountryEntity country, Optional<CountryEntity> toUpdate) {
        CountryEntity existingCountry = toUpdate.get();
        existingCountry.setName(country.getName());
        existingCountry.setPopulation(country.getPopulation());
        existingCountry.setSurfaceArea(country.getSurfaceArea());
        existingCountry.setIndepYear(country.getIndepYear());
        existingCountry.setLifeExpectancy(country.getLifeExpectancy());
        existingCountry.setGnp(country.getGnp());
        existingCountry.setGNPOld(country.getGNPOld());
        existingCountry.setLocalName(country.getLocalName());
        existingCountry.setGovernmentForm(country.getGovernmentForm());
        existingCountry.setHeadOfState(country.getHeadOfState());
        existingCountry.setCapital(country.getCapital());
        existingCountry.setCode2(country.getCode2());
        return existingCountry;
    }

    public boolean updateLanguageById(CountryLanguageIdEntity id, CountryLanguageEntity lang) {
        if (id == null || lang == null || lang.getPercentage().compareTo(BigDecimal.ZERO) < 0 || lang.getPercentage().compareTo(BigDecimal.valueOf(100)) > 0) {
            return false;
        }

        Optional<CountryLanguageEntity> toUpdate = countryLanguageRepository.findById(id);
        if (toUpdate.isPresent()) {
            CountryLanguageEntity existingLanguage = toUpdate.get();
            existingLanguage.setIsOfficial(lang.getIsOfficial());
            existingLanguage.setPercentage(lang.getPercentage());
            countryLanguageRepository.save(existingLanguage);
            return true;
        }
        return false;
    }

    public double whatPercentageOfPopulationLivesInLargestCityIn(String countryCode) {
        Optional<CountryEntity> country = countryRepository.findById(countryCode);

        if (!country.isPresent()) {
            logger.info("Country with CountryCode:" + countryCode + " not found.");
            return 0;
        }

        List<CityEntity> allCitiesInCountry = getAllCitiesInCountry(countryCode);
        CityEntity largestCity = getLargestCity(allCitiesInCountry);

        double countryPopulation = country.get().getPopulation();
        double largestCityPopulation = largestCity.getPopulation();
        double percentageOfPopulationInLargestCity = (largestCityPopulation / countryPopulation) * 100;
        String formattedPercentage = String.format("%.2f", percentageOfPopulationInLargestCity);
        logger.info("The largest city in " + country.get().getName() +
                " is " + largestCity.getName() +
                " and " + formattedPercentage +
                "% of the total population lives there.");
        return percentageOfPopulationInLargestCity;
    }

    private List<CityEntity> getAllCitiesInCountry(String countryCode) {
        CountryEntity country = countryRepository.findById(countryCode).get();
        List<CityEntity> allCities = cityRepository.findAll();
        List<CityEntity> citiesInCountry = new ArrayList<>();
        for (CityEntity city : allCities) {
            if (city.getCountryCode().getCode().equals(countryCode)) {
                citiesInCountry.add(city);
            }
        }
        return citiesInCountry;
    }

    private CityEntity getLargestCity(List<CityEntity> cities) {
        CityEntity largestCity = cities.get(0);
        for (CityEntity city : cities) {
            if (city.getPopulation() > largestCity.getPopulation()) {
                largestCity = city;
            }
        }
        return largestCity;
    }

    public List<Pair<String, Integer>> findFiveSmallestDistricts() {
        Map<String, Integer> districts = getDistrictPopulationMap();

        return districts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<Pair<String, Integer>> findFiveLargestDistricts() {
        Map<String, Integer> districts = getDistrictPopulationMap();

        return districts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getDistrictPopulationMap() {
        Map<String, Integer> districts = new HashMap<>();

        cityRepository.findAll().stream().
                filter(city -> city.getDistrict() != null).
                forEach(city -> districts.put(city.getDistrict(), districts.getOrDefault(city.getDistrict(), 0) + city.getPopulation()));
        return districts;
    }

    public Pair<String, Integer> getCountryWithMostCities() {
        Set<CityEntity> citiesSet = new HashSet<>(cityRepository.findAll());
        Map<String, Integer> countryCityCountMap = new HashMap<>();

        for (CityEntity city : citiesSet) {
            String countryCode = city.getCountryCode().getCode();
            countryCityCountMap.put(countryCode, countryCityCountMap.getOrDefault(countryCode, 0) + 1);
        }

        String countryWithMostCities = null;
        int mostCities = 0;

        for (Map.Entry<String, Integer> entry : countryCityCountMap.entrySet()) {
            if (entry.getValue() > mostCities) {
                mostCities = entry.getValue();
                countryWithMostCities = entry.getKey();
            }
        }
        return Pair.of(countryWithMostCities, mostCities);
    }

    public Optional<CountryEntity> getCountryByName(String countryName) {
        return countryRepository.findCountryEntityByName(countryName);
    }

    public List<CountryEntity> getCountriesByNameLike(String countryName) {
        return countryRepository.findByNameContaining(countryName);
    }

    public List<CountryEntity> getCountriesByContinent(String continent) {
        return countryRepository.findCountryEntitiesByContinent(continent);
    }

    public List<CountryEntity> getCountriesByRegion(String region) {
        return countryRepository.findCountryEntitiesByRegion(region);
    }

    public List<CountryEntity> getCountriesByPopulationBetween(int lowEnd, int highEnd) {
        return countryRepository.findCountryEntitiesByPopulationBetween(lowEnd, highEnd);
    }

    public CityEntity getCityById(Integer id) {
        Optional<CityEntity> cityOptional = cityRepository.findById(id);
        if (cityOptional.isPresent()) {
            return cityOptional.get();
        } else {
            throw new IllegalArgumentException("City with ID " + id + " not found");
        }
    }

    public List<CityEntity> getAllCities() {
        return cityRepository.findAll();
    }

    public List<CityEntity> getCitiesByCountryCode(String countryCode) {
        List<CityEntity> cities = cityRepository.findAll().stream().
                filter(city -> city.getCountryCode().getCode().equals(countryCode))
                .toList();
        if (cities.isEmpty()) {
            throw new IllegalArgumentException("Cities with country code " + countryCode + " not found");
        } else {
            return cities;
        }

    }

    public List<CityEntity> getCitiesByName(String name) {
        List<CityEntity> cities = cityRepository.findAll();
        if (name.isEmpty()) {
            return List.of();
        }
        return cities.stream()
                .filter(city -> city.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public List<CityEntity> getCitiesByDistrict(String district) {
        List<CityEntity> cities = cityRepository.findAll();
        if (district.isEmpty()) {
            return List.of();
        }
        cities = cities.stream()
                .filter(city -> city.getDistrict().toLowerCase().contains(district.toLowerCase()))
                .toList();
        if (cities.isEmpty()) {
            throw new IllegalArgumentException("No cities found for district " + district);
        } else {
            return cities;
        }
    }

    public List<CityEntity> getCitiesByMinPopulation(int minPopulation) {
        List<CityEntity> cities = cityRepository.findAll();
        if (minPopulation >= 0) {
            return cities.stream()
                    .filter(city -> city.getPopulation() >= minPopulation)
                    .collect(Collectors.toList());

        } else {
            throw new IllegalArgumentException("You cannot enter a negative population of: " + minPopulation);
        }
    }

    public List<CityEntity> getCitiesByMaxPopulation(int maxPopulation) {
        List<CityEntity> cities = cityRepository.findAll();
        if (maxPopulation >= 0) {
            return cities.stream()
                    .filter(city -> city.getPopulation() <= maxPopulation)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("You cannot enter a negative population of: " + maxPopulation);
        }

    }
}
