package com.mjolnir.yggdrasil.controllers;

import com.mjolnir.yggdrasil.dto.CityDTO;
import com.mjolnir.yggdrasil.dto.CityDeletionResponseDTO;
import com.mjolnir.yggdrasil.dto.DistrictDTO;
import com.mjolnir.yggdrasil.entities.CityEntity;
import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
import com.mjolnir.yggdrasil.repositories.CityRepository;
import com.mjolnir.yggdrasil.repositories.CountryLanguageRepository;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.MjolnirApiService;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Yggdrasil")
public class CityController {
    private final WorldService worldService;
    private final MjolnirApiService mjolnirApiService;


    public CityController(CityRepository cityRepository, CountryRepository countryRepository, CountryLanguageRepository countryLanguageRepository, WorldService worldService, CityDTO cityDTO, MjolnirApiService mjolnirApiService) {
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

    @GetMapping("/cities/search/cityId")
    public EntityModel<Optional<CityEntity>> getCityById(@RequestParam Integer id) {
        Optional<CityEntity> city = worldService.getCityOptionalById(id);
        if (city.isPresent()) {return EntityModel.of(city,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("cities"));
        } else {
            throw new ResourceNotFoundException("City with id " + id + " not found");
        }
    }

    @GetMapping("/cities/search/countryCode")
    public ResponseEntity<CollectionModel<EntityModel<CityEntity>>> searchCitiesByCountryCode(@RequestParam String code) {

        if (code.length() != 3 || !code.matches("[A-Z]{3}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Country code must be exactly 3 uppercase letters.");
        }
        if (!worldService.isValidCountryCode(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid country code.");
        }

        List<CityEntity> cities = worldService.getCitiesByCountryCode(code);
        //This check below may not need to be here.
        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found for country code: " + code);
        }

        List<EntityModel<CityEntity>> cityModels = cities.stream()
                .map(city -> EntityModel.of(city,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(city.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("cities")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(cityModels,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withSelfRel())
        );
    }

    @GetMapping("/cities/search/districts/largest")
    public ResponseEntity<CollectionModel<EntityModel<DistrictDTO>>> findFiveLargestDistricts() {
        List<Pair<String, Integer>> districts = worldService.findFiveLargestDistricts();

        List<EntityModel<DistrictDTO>> districtModels = districts.stream()
                .map(district -> {
                    DistrictDTO dto = new DistrictDTO(district.getFirst(), district.getSecond());
                    return EntityModel.of(dto,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).findFiveLargestDistricts()).withSelfRel());
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DistrictDTO>> collectionModel = CollectionModel.of(districtModels,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).findFiveLargestDistricts()).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/cities/search/districts/smallest")
    public ResponseEntity<CollectionModel<EntityModel<DistrictDTO>>> findFiveSmallestDistricts() {
        List<Pair<String, Integer>> districts = worldService.findFiveSmallestDistricts();

        List<EntityModel<DistrictDTO>> districtModels = districts.stream()
                .map(pair -> {
                    DistrictDTO dto = new DistrictDTO(pair.getFirst(), pair.getSecond());
                    return EntityModel.of(dto,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).findFiveSmallestDistricts()).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(pair.getSecond())).withRel("city"));
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DistrictDTO>> collectionModel = CollectionModel.of(districtModels,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).findFiveSmallestDistricts()).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities"));

        return ResponseEntity.ok(collectionModel);
    }



    @GetMapping("/cities/search/district")
    public ResponseEntity<CollectionModel<EntityModel<CityEntity>>> getCitiesByDistrict(@RequestParam String districtName) {
        List<CityEntity> cities = worldService.getCitiesByDistrict(districtName);

        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found for district: " + districtName);
        }

        List<EntityModel<CityEntity>> cityModels = cities.stream()
                .map(city -> EntityModel.of(city,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(city.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCitiesByDistrict(districtName)).withRel("district-cities"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CityEntity>> collectionModel = CollectionModel.of(cityModels,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCitiesByDistrict(districtName)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/cities/search/populationBelow")
    public ResponseEntity<CollectionModel<EntityModel<CityEntity>>> getCitiesByMaxPopulation(
            @RequestParam("population") int maxPopulation) {

        List<CityEntity> cities = worldService.getCitiesByMaxPopulation(maxPopulation);

        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found with a population less than or equal to " + maxPopulation);
        }

        List<EntityModel<CityEntity>> cityModels = cities.stream()
                .map(city -> EntityModel.of(city,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(city.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCitiesByMaxPopulation(maxPopulation)).withRel("by-max-population"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CityEntity>> collectionModel = CollectionModel.of(cityModels,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCitiesByMaxPopulation(maxPopulation)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/cities/search/populationAbove")
    public ResponseEntity<CollectionModel<EntityModel<CityEntity>>> getCitiesByMinPopulation(
            @RequestParam("population") int minPopulation) {

        List<CityEntity> cities = worldService.getCitiesByMinPopulation(minPopulation);

        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found with a population greater than or equal to " + minPopulation);
        }

        List<EntityModel<CityEntity>> cityModels = cities.stream()
                .map(city -> EntityModel.of(city,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(city.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCitiesByMinPopulation(minPopulation)).withRel("by-min-population"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CityEntity>> collectionModel = CollectionModel.of(cityModels,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCitiesByMinPopulation(minPopulation)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/cities/search/name")
    public ResponseEntity<CollectionModel<EntityModel<CityEntity>>> getCitiesByName(@RequestParam("cityName") String name) {
        List<CityEntity> cities = worldService.getCitiesByName(name);

        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found with the name: " + name);
        }

        List<EntityModel<CityEntity>> cityModels = cities.stream()
                .map(city -> EntityModel.of(city,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCityById(city.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCitiesByName(name)).withRel("by-name"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CityEntity>> collectionModel = CollectionModel.of(cityModels,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getCitiesByName(name)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CityController.class).getAllCities()).withRel("all-cities"));

        return ResponseEntity.ok(collectionModel);
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
            worldService.deleteCityById(cityOptional.get().getId());

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
