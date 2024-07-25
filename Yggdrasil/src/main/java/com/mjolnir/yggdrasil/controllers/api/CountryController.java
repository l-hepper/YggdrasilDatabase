package com.mjolnir.yggdrasil.controllers.api;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.MjolnirApiService;
import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public String searchByCode(@RequestParam("code") String code, Model model) {
        Optional<CountryEntity> country = worldService.getCountryByCode(code);
        if (country.isPresent()) {
            model.addAttribute("countriesList", List.of(country.get()));
        } else {
            model.addAttribute("message", "Country not found.");
        }
        return "countries";
    }

    // Method to render the update form with the country data
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

    @PostMapping("/update")
    public String updateCountry(@ModelAttribute CountryEntity updatedCountry, Model model) {

        worldService.updateCountryById(updatedCountry.getCode(), updatedCountry);

        List<CountryEntity> countriesList = worldService.getAllCountries();
        model.addAttribute("countriesList", countriesList);

        return "countries";
    }

    @PostMapping("/delete/{id}")
    public String deleteCountry(@PathVariable String id) {
        boolean deleted = worldService.deleteCountryByCode(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        return "countries";
    }

//
//    @PostMapping
//    public ResponseEntity<EntityModel<CountryEntity>> createCountry(@RequestBody CountryEntity country, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey, HttpServletRequest request) {
//        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
//        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");
//
//        CountryEntity countryEntity = null;
//        try {
//            worldService.createNewCountry(
//                    country.getCode(),
//                    country.getName(),
//                    country.getContinent(),
//                    country.getRegion(),
//                    country.getSurfaceArea(),
//                    country.getIndepYear(),
//                    country.getPopulation(),
//                    country.getLifeExpectancy(),
//                    country.getGnp(),
//                    country.getGNPOld(),
//                    country.getLocalName(),
//                    country.getGovernmentForm(),
//                    country.getHeadOfState(),
//                    country.getCode2(),
//                    true
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        EntityModel<CountryEntity> resource = EntityModel.of(worldService.getCountryByCode(country.getCode()).get());
////        resource.add(linkTo(methodOn(CountryController.class).getCountry(country.getCode())).withSelfRel());
////        resource.add(linkTo(methodOn(CountryController.class).getCountries()).withRel("all-countries"));
//        return new ResponseEntity<>(resource, HttpStatus.OK);
//    }

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
