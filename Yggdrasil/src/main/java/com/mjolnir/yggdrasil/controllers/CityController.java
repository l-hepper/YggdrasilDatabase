package com.mjolnir.yggdrasil.controllers;

import com.mjolnir.yggdrasil.dto.CityDTO;
import com.mjolnir.yggdrasil.dto.CityDeletionResponseDTO;
import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.MjolnirApiService;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/Yggdrasil")
public class CityController {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CountryLanguageRepository countryLanguageRepository;
    private final WorldService worldService;
    private final MjolnirApiService mjolnirApiService;


    public CityController(CityRepository cityRepository, CountryRepository countryRepository, CountryLanguageRepository countryLanguageRepository, WorldService worldService, CityDTO cityDTO, MjolnirApiService mjolnirApiService) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.countryLanguageRepository = countryLanguageRepository;
        this.worldService = worldService;
        this.mjolnirApiService = mjolnirApiService;
    }

    @GetMapping("/cities")
    public CollectionModel<EntityModel<CityEntity>> getAllCities() {
        List<CityEntity> cities = worldService.getAllCities();

        List<EntityModel<CityEntity>> cityModels = cities.stream()
                .map(city -> EntityModel.of(city,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(city.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("cities")))
                .collect(Collectors.toList());

        return CollectionModel.of(cityModels,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withSelfRel());
    }

    @GetMapping("/cities/{id}")
    public EntityModel<Optional<CityEntity>> getCityById(@PathVariable Integer id) {
        Optional<CityEntity> city = worldService.getCityOptionalById(id);
        if (city.isPresent()) {return EntityModel.of(city,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("cities"));
        } else {
            throw new ResourceNotFoundException("City with id " + id + " not found");
        }
    }

    @PostMapping("/cities")
    public ResponseEntity<EntityModel<CityEntity>> createCity(@RequestBody CityDTO cityDTO, HttpServletRequest request, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");

        String name = cityDTO.getName();
        String district = cityDTO.getDistrict();
        Integer population = cityDTO.getPopulation();
        String countryCode = cityDTO.getCountryCode();
        boolean isCapital = cityDTO.isCapital();

        worldService.createNewCity(countryCode, name, district, population, isCapital);

        return getEntityModelResponseEntity(request, name);
    }

    @DeleteMapping("/cities/{id}")
    public ResponseEntity<EntityModel<CityDeletionResponseDTO>> deleteCity(@PathVariable Integer id, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");

        Optional<CityEntity> cityOptional = worldService.getCityOptionalById(id);

        if (cityOptional.isPresent()) {
            CityEntity cityEntity = cityOptional.get();
            cityRepository.delete(cityOptional.get());

            String message = String.format(
                    "The city with ID: %d, named %s, located in the district of %s, with a population of %d has been successfully deleted.",
                    cityEntity.getId(), cityEntity.getName(), cityEntity.getDistrict(), cityEntity.getPopulation()
            );

            CityDeletionResponseDTO response = new CityDeletionResponseDTO(message, cityOptional.get());

            EntityModel<CityDeletionResponseDTO> entityModel = EntityModel.of(response,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("cities"));
            return ResponseEntity.ok(entityModel);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City with id " + id + " not found.");
        }
    }

    @PatchMapping("cities/{id}")
    public ResponseEntity<EntityModel<CityEntity>> updateCity(@PathVariable Integer id, @RequestBody CityDTO city, HttpServletRequest request, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");
        String name = city.getName();
        String district = city.getDistrict();
        Integer population = city.getPopulation();
        boolean isCapital = city.isCapital();

        Optional<CityEntity> cityOptional = worldService.getCityOptionalById(id);
        if (cityOptional.isEmpty()) {
            throw new ResourceNotFoundException("City with id: " + id + " not found.");
        }

        if (!Objects.equals(id, cityOptional.get().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        worldService.updateCityById(id, city);
        return getEntityModelResponseEntity(request, name);
    }

    private ResponseEntity<EntityModel<CityEntity>> getEntityModelResponseEntity(HttpServletRequest request, String name) {
        CityEntity cityEntity = worldService.getCitiesByName(name).getFirst();

        EntityModel<CityEntity> cityModel = EntityModel.of(cityEntity,
                WebMvcLinkBuilder.linkTo(CityController.class).slash("cities").slash(cityEntity.getId()).withSelfRel(),
                WebMvcLinkBuilder.linkTo(CityController.class).slash("cities").withRel("cities"));

        URI location = URI.create(request.getRequestURL().toString() + "/" + cityEntity.getId());
        return ResponseEntity.created(location).body(cityModel);
    }
}
