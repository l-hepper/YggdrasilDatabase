package com.mjolnir.yggdrasil.dto;

import org.springframework.hateoas.RepresentationModel;

public class DistrictDTO extends RepresentationModel<DistrictDTO> {
    private String cityName;
    private int population;

    public DistrictDTO(String cityName, int population) {
        this.cityName = cityName;
        this.population = population;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
