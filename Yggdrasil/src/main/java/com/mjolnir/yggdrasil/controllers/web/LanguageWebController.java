package com.mjolnir.yggdrasil.controllers.web;

import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
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
            return "redirect:search?searchMethod=countryCode&countryCode=" + language.getId().getCountryCode() + "&language=&percentageBelow=&percentageAbove=";
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

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String searchLanguages(
            @RequestParam(required = false) String searchMethod,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean isOfficial,
            @RequestParam(required = false) BigDecimal percentageBelow,
            @RequestParam(required = false) BigDecimal percentageAbove,
            Model model) {

        List<CountryLanguageEntity> languages = new ArrayList<>();
        try {
            switch (searchMethod) {
                case "countryCode":
                    if (countryCode != null) {
                        languages = worldService.getLanguagesByCountryCode(countryCode);
                    } else {
                        throw new IllegalArgumentException("countryCode is required for search by countryCode");
                    }
                    break;
                case "language":
                    if (language != null) {
                        languages = worldService.getLanguagesByLanguage(language);
                    } else {
                        throw new IllegalArgumentException("languages is required for search by languages");
                    }
                    break;
                case "isOfficial":
                    if (isOfficial != null) {
                        languages = worldService.getLanguagesByIsOfficial(isOfficial);
                    } else {
                        throw new IllegalArgumentException("isOfficial is required for search by isOfficial");
                    }
                    break;
                case "percentageBelow":
                    if (percentageBelow != null) {
                        languages = worldService.getLanguagesByPercentageBelow(percentageBelow);
                    } else {
                        throw new IllegalArgumentException("percentage is required for search by percentage");
                    }
                    break;
                case "percentageAbove":
                    if (percentageAbove != null) {
                        languages = worldService.getLanguagesByPercentageAbove(percentageAbove);
                    } else {
                        throw new IllegalArgumentException("percentage is required for search by percentage");
                    }
                    break;
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        model.addAttribute("languages", languages);
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
            throw new UpdateFailedException("An error occurred while deleting language: " + countryCode + " " + language);
        }
    }
}
