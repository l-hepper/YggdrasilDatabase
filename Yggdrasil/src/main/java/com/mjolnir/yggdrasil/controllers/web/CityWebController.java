package com.mjolnir.yggdrasil.controllers.web;

import com.mjolnir.yggdrasil.dto.CityDTO;
import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
