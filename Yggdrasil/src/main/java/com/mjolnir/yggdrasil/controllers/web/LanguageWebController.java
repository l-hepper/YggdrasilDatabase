package com.mjolnir.yggdrasil.controllers.web;

import com.mjolnir.yggdrasil.dto.CityDTO;
import com.mjolnir.yggdrasil.entities.CountryLanguageIdEntity;
import com.mjolnir.yggdrasil.exceptions.UpdateFailedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import com.mjolnir.yggdrasil.entities.CountryLanguageEntity;
import com.mjolnir.yggdrasil.service.WorldService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/languages")
public class LanguageWebController {
    private final WorldService worldService;

    public LanguageWebController(final WorldService worldService) {
        this.worldService = worldService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createCity(@ModelAttribute CountryLanguageEntity language, Model model) {
        try {
            worldService.createNewCountryLanguage(language.getId().getCountryCode(), language.getLanguage(), language.getIsOfficial(), language.getPercentage());
            return "redirect:/languages/" + language.getId().getCountryCode();
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while creating the city.");
            return "languages/create";
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getLanguageById(Model model) {
        List<CountryLanguageEntity> languages = worldService.getAllLanguages();
        model.addAttribute("languages", languages);
        return "languages";
    }

    @GetMapping("/{countryCode}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getAllLanguages(@PathVariable String countryCode, Model model) {
        List<CountryLanguageEntity> languages = worldService.getLanguagesByCountryCode(countryCode);
        model.addAttribute("languages", languages);
        return "languages";
    }

    @GetMapping("/{countryCode}/{language}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getAllLanguages(@PathVariable String countryCode, @PathVariable String language, Model model) {
        Optional<CountryLanguageEntity> languageEntity = worldService.getLanguageById(countryCode, language);
        if (languageEntity.isPresent()) {
            List<CountryLanguageEntity> languages = new ArrayList<>();
            languages.add(languageEntity.get());
            model.addAttribute("languages", languages);
        } else {
            model.addAttribute("noLanguageFound", true);
        }
        return "languages";
    }

    @PatchMapping("/{countryCode}/{language}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateLanguage(@PathVariable String countryCode, @PathVariable String language, @RequestBody Map<String, Object> updates, Model model) {
        Optional<CountryLanguageEntity> toUpdate = worldService.getLanguageById(countryCode, language);
        if (toUpdate.isPresent()) {
            if (updates.containsKey("isOfficial")) {
                toUpdate.get().setIsOfficial((String) updates.get("isOfficial"));
            }
            if (updates.containsKey("percentage")) {
                toUpdate.get().setPercentage(BigDecimal.valueOf((double) updates.get("percentage")));
            }

            boolean isUpdated = worldService.updateLanguageById(toUpdate.get().getId(), toUpdate.get());
            if (isUpdated) {
                return "languages";
            } else {
                throw new UpdateFailedException("An error occurred while updating language: " + countryCode + " " + language);
            }
        } else {
            throw new UpdateFailedException("An error occurred while updating language: " + countryCode + " " + language);
        }
    }

    @DeleteMapping("/{countryCode}/{language}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteLanguage(@PathVariable String countryCode, @PathVariable String language) {
        boolean isDeleted = worldService.deleteLanguageByCountryCodeAndLanguage(countryCode, language);
        if (isDeleted) {
            return "languages";
        } else {
            return "redirect:/languages/" + countryCode + "/" + language;
        }
    }
}
