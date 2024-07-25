package com.mjolnir.yggdrasil.controllers.api;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.MjolnirApiService;
import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/Yggdrasil/countries")
public class CountryController {

    private final MjolnirApiService mjolnirApiService;
    private final WorldService worldService;
    private final CountryRepository countryRepository;

    @Autowired
    public CountryController(MjolnirApiService mjolnirApiService, WorldService worldService, CountryRepository countryRepository) {
        this.mjolnirApiService = mjolnirApiService;
        this.worldService = worldService;
        this.countryRepository = countryRepository;
    }

    @GetMapping()
    public String getCountries(Model model) {
        List<CountryEntity> countriesList = countryRepository.findAll();
        model.addAttribute("countriesList", countriesList);
        return "/countries";
    }

    @GetMapping("/search")
    public String searchCountries(@RequestParam(required = false, defaultValue = "") String searchMethod,
                                  @RequestParam(name = "countryCode", required = false) String countryCode,
                                  @RequestParam(required = false, defaultValue = "") String name,
                                  @RequestParam(required = false, defaultValue = "") String continent,
                                  @RequestParam(required = false, defaultValue = "") String region,
                                  @RequestParam(required = false, defaultValue = "") String governmentForm,
                                  Model model) {

        switch (searchMethod) {
            case "id":
                if (countryCode != null && !countryCode.isEmpty()) {
                    CountryEntity country = countryRepository.findById(countryCode).get();
                    System.out.println("Found Country: " + country.getCode());
                    model.addAttribute("country", country);
                }
                break;
            case "name":
                if (name != null && !name.isEmpty()) {
                    CountryEntity country = countryRepository.findCountryEntityByName(name).get();
                    System.out.println("Found Country: " + country.getName());
                    model.addAttribute("country", country);
                }
                break;
            case "continent":
                if (continent != null && !continent.isEmpty()) {
                    List<CountryEntity> countries = countryRepository.findCountryEntitiesByContinent(continent);
                    model.addAttribute("country", countries);
                }
                break;
            case "region":
                if (region != null && !region.isEmpty()) {
                    List<CountryEntity> countries = countryRepository.findCountryEntitiesByRegion(region);
                    model.addAttribute("country", countries);
                }
                break;
            case "governmentForm":
                if (governmentForm != null && !governmentForm.isEmpty()) {
                    List<CountryEntity> countries = countryRepository.findCountryEntitiesByGovernmentForm(governmentForm);
                    model.addAttribute("country", countries);
                }
                break;
        }
        return "countries/searchResults";
    }

    @GetMapping("/edit/{code}")
    public String editCountry(@PathVariable String code, Model model) {
        Optional<CountryEntity> country = worldService.getCountryByCode(code);
        if (country.isPresent()) {
            model.addAttribute("countryToEdit", country.get());
        } else {
            model.addAttribute("message", "Country not found.");
        }
        return "countries";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCountry(@PathVariable String id) {
        boolean deleted = worldService.deleteCountryByCode(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        return "countries";
    }

    @PostMapping("/update/{countryCode}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateCountry(@PathVariable String countryCode, @RequestBody Map<String, Object> updates, Model model) {
        Optional<CountryEntity> toUpdate = worldService.getCountryByCode(countryCode);

        if (toUpdate.isPresent()) {
            CountryEntity country = toUpdate.get();

            if (updates.containsKey("name")) {
                country.setName((String) updates.get("name"));
            }
            if (updates.containsKey("continent")) {
                country.setContinent((String) updates.get("continent"));
            }
            if (updates.containsKey("region")) {
                country.setRegion((String) updates.get("region"));
            }
            if (updates.containsKey("surfaceArea")) {
                Object surfaceAreaValue = updates.get("surfaceArea");
                if (surfaceAreaValue instanceof Number) {
                    country.setSurfaceArea(BigDecimal.valueOf(((Number) surfaceAreaValue).doubleValue()));
                } else {
                    throw new IllegalArgumentException("Invalid value for surfaceArea: " + surfaceAreaValue);
                }
            }
            if (updates.containsKey("indepYear")) {
                Object indepYearValue = updates.get("indepYear");
                if (indepYearValue instanceof Number) {
                    country.setIndepYear(((Number) indepYearValue).shortValue());
                } else {
                    throw new IllegalArgumentException("Invalid value for indepYear: " + indepYearValue);
                }
            }
            if (updates.containsKey("population")) {
                Object populationValue = updates.get("population");
                if (populationValue instanceof Number) {
                    country.setPopulation(((Number) populationValue).intValue());
                } else {
                    throw new IllegalArgumentException("Invalid value for population: " + populationValue);
                }
            }
            if (updates.containsKey("lifeExpectancy")) {
                Object lifeExpectancyValue = updates.get("lifeExpectancy");
                if (lifeExpectancyValue instanceof Number) {
                    country.setLifeExpectancy(BigDecimal.valueOf(((Number) lifeExpectancyValue).doubleValue()));
                } else {
                    throw new IllegalArgumentException("Invalid value for lifeExpectancy: " + lifeExpectancyValue);
                }
            }
            if (updates.containsKey("gnp")) {
                Object gnpValue = updates.get("gnp");
                if (gnpValue instanceof Number) {
                    country.setGnp(BigDecimal.valueOf(((Number) gnpValue).doubleValue()));
                } else {
                    throw new IllegalArgumentException("Invalid value for gnp: " + gnpValue);
                }
            }
            if (updates.containsKey("gNPOld")) {
                Object gnpOldValue = updates.get("gNPOld");
                if (gnpOldValue instanceof Number) {
                    country.setGNPOld(BigDecimal.valueOf(((Number) gnpOldValue).doubleValue()));
                } else {
                    throw new IllegalArgumentException("Invalid value for gNPOld: " + gnpOldValue);
                }
            }
            if (updates.containsKey("localName")) {
                country.setLocalName((String) updates.get("localName"));
            }
            if (updates.containsKey("governmentForm")) {
                country.setGovernmentForm((String) updates.get("governmentForm"));
            }
            if (updates.containsKey("headOfState")) {
                country.setHeadOfState((String) updates.get("headOfState"));
            }
            if (updates.containsKey("capital")) {
                Object capitalValue = updates.get("capital");
                if (capitalValue instanceof Number) {
                    country.setCapital(((Number) capitalValue).intValue());
                } else {
                    throw new IllegalArgumentException("Invalid value for capital: " + capitalValue);
                }
            }
            if (updates.containsKey("code2")) {
                country.setCode2((String) updates.get("code2"));
            }


                // Save the updated country
                boolean isUpdated = worldService.updateCountryById(countryCode, country);
                if (isUpdated) {
                    // Redirect or return appropriate response
                    List<CountryEntity> countriesList = worldService.getAllCountries();
                    model.addAttribute("countriesList", countriesList);
                    return "countries"; // Redirect or return view
                } else {
//                    throw new UpdateFailedException("An error occurred while updating country: " + countryCode);
                    return "";
                }
            } else {
//                throw new UpdateFailedException("Country not found: " + countryCode);
                return "";
            }
        }


    @PostMapping("/create")
    public String createCountry(@ModelAttribute CountryEntity country, Model model) {

        CountryEntity countryEntity = null;
        try {
            worldService.createNewCountry(
                    country.getCode(),
                    country.getName(),
                    country.getContinent(),
                    country.getRegion(),
                    country.getSurfaceArea(),
                    country.getIndepYear(),
                    country.getPopulation(),
                    country.getLifeExpectancy(),
                    country.getGnp(),
                    country.getGNPOld(),
                    country.getLocalName(),
                    country.getGovernmentForm(),
                    country.getHeadOfState(),
                    country.getCode2(),
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("countriesList", countryRepository.findAll());
        return "countries_view";
    }

//        CountryEntity country = optionalCountry.get();

//

//    @PatchMapping("/{id}")
//    public ResponseEntity<EntityModel<CountryEntity>> partialUpdateCountry(@PathVariable String id, @RequestBody Map<String, Object> updates, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey, HttpServletRequest request) {
//        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
//        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");
//        Optional<CountryEntity> optionalCountry = worldService.getCountryByCode(id);
//        if (!optionalCountry.isPresent()) {
//            throw new ResourceNotFoundException("Country with id " + id + " not found");
//        }
//
//        CountryEntity country = optionalCountry.get();
//
//        for (Map.Entry<String, Object> entry : updates.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//
//            switch (key) {
//                case "name" -> country.setName((String) value);
//                case "continent" -> country.setContinent((String) value);
//                case "region" -> country.setRegion((String) value);
//                case "surfaceArea" -> country.setSurfaceArea(new BigDecimal(value.toString()));
//                case "indepYear" -> country.setIndepYear(Short.parseShort(value.toString()));
//                case "population" -> country.setPopulation(Integer.parseInt(value.toString()));
//                case "lifeExpectancy" -> country.setLifeExpectancy(new BigDecimal(value.toString()));
//                case "gnp" -> country.setGnp(new BigDecimal(value.toString()));
//                case "localName" -> country.setLocalName((String) value);
//                case "governmentForm" -> country.setGovernmentForm((String) value);
//                case "headOfState" -> country.setHeadOfState((String) value);
//                case "capital" -> country.setCapital(Integer.parseInt(value.toString()));
//                case "code2" -> country.setCode2((String) value);
//                case "gnpold" -> country.setGNPOld(new BigDecimal(value.toString()));
//                default -> throw new ResourceNotFoundException("The field '" + key + "' is not valid.");
//            }
//        }
//
//        CountryEntity updatedCountry = countryRepository.save(country);
//        EntityModel<CountryEntity> resource = EntityModel.of(worldService.getCountryByCode(country.getCode()).get());
////        resource.add(linkTo(methodOn(CountryController.class).getCountry(country.getCode())).withSelfRel());
//        return new ResponseEntity<>(resource, HttpStatus.OK);
//    }
}
