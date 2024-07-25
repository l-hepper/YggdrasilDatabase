package com.mjolnir.yggdrasil.controllers.api;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.MjolnirApiService;
import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        return "countries_view";
    }

    @GetMapping("/search")
    public String searchCountries(@RequestParam(required = false, defaultValue = "") String searchMethod,
                                  @RequestParam(name = "countryCode", required = false) String countryCode,
                                  @RequestParam(required = false, defaultValue = "") String name,
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
        }

        return "countries/searchResults"; // The name of the HTML template to display results
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
        return "countries_view";
    }

    @PostMapping("/update")
    public String updateCountry(@ModelAttribute CountryEntity updatedCountry, Model model) {

        worldService.updateCountryById(updatedCountry.getCode(), updatedCountry);

        List<CountryEntity> countriesList = worldService.getAllCountries();
        model.addAttribute("countriesList", countriesList);

        return "countries_view";
    }

    @PostMapping("/delete/{id}")
    public String deleteCountry(@PathVariable String id) {
        boolean deleted = worldService.deleteCountryByCode(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        return "countries_view";
    }

//
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
