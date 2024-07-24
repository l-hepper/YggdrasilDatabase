package com.mjolnir.yggdrasil.controllers.web;

import com.mjolnir.yggdrasil.dto.CityDTO;
import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class CityWebController {
    private final WorldService worldService;

    public CityWebController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping("/cities")
    public String getAllCitiesPage(Model model) {
        List<CityEntity> cities = worldService.getAllCities();
        model.addAttribute("cities", cities);
        return "cities";
    }

//    @GetMapping("/cities/{id}")
//    public String getCityPage(@PathVariable int id, Model model) {
//        CityEntity city = worldService.getCityById(id);
//        model.addAttribute("city", city);
//        return "cities/{id}";
//    }

    @GetMapping("/cities/search")
    public String searchCities(
            @RequestParam(required = false, defaultValue = "") String searchMethod,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String countryCode,
            @RequestParam(required = false) Integer largestDistricts,
            @RequestParam(required = false) Integer smallestDistricts,
            @RequestParam(required = false, defaultValue = "") String district,
            @RequestParam(required = false) Integer populationBelow,
            @RequestParam(required = false) Integer populationAbove,
            Model model) {

        List<CityEntity> cities = new ArrayList<>();

        try {
            switch (searchMethod) {
                case "id":
                    if (cityId != null) {
                        CityEntity city = worldService.getCityById(cityId);
                        if (city != null) {
                            cities.add(city);
                        }
                    } else {
                        throw new IllegalArgumentException("cityId is required for search by ID");
                    }
                    break;
                case "name":
                    if (!name.isEmpty()) {
                        cities = worldService.getCitiesByName(name);
                    } else {
                        throw new IllegalArgumentException("Name is required for search by name");
                    }
                    break;
                case "countryCode":
                    if (!countryCode.isEmpty()) {
                        cities = worldService.getCitiesByCountryCode(countryCode);
                    } else {
                        throw new IllegalArgumentException("Country Code is required for search by country code");
                    }
                    break;
//                case "largestDistricts":
//                    if (largestDistricts != null) {
//                        cities = worldService.findFiveLargestDistricts();
//                    } else {
//                        throw new IllegalArgumentException("Number of largest districts is required");
//                    }
//                    break;
//                case "smallestDistricts":
//                    if (smallestDistricts != null) {
//                        cities = worldService.findFiveSmallestDistricts();
//                    } else {
//                        throw new IllegalArgumentException("Number of smallest districts is required");
//                    }
//                    break;
                case "byDistrict":
                    if (!district.isEmpty()) {
                        cities = worldService.getCitiesByDistrict(district);
                    } else {
                        throw new IllegalArgumentException("District is required for search by district");
                    }
                    break;
                case "populationBelow":
                    if (populationBelow != null) {
                        cities = worldService.getCitiesByMinPopulation(populationBelow);
                    } else {
                        throw new IllegalArgumentException("Population below value is required");
                    }
                    break;
                case "populationAbove":
                    if (populationAbove != null) {
                        cities = worldService.getCitiesByMaxPopulation(populationAbove);
                    } else {
                        throw new IllegalArgumentException("Population above value is required");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid search method");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while performing the search.");
        }

        model.addAttribute("cities", cities);
        return "cities/searchResults";
    }







    @PostMapping("/cities/create")
    public String createCity(@ModelAttribute CityDTO cityDTO, Model model) {
        try {
            worldService.createNewCity(
                    cityDTO.getCountryCode(),
                    cityDTO.getName(),
                    cityDTO.getDistrict(),
                    cityDTO.getPopulation(),
                    cityDTO.isCapital()
            );
            return "redirect:/cities";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while creating the city.");
            return "cities/create";
        }
    }




}
