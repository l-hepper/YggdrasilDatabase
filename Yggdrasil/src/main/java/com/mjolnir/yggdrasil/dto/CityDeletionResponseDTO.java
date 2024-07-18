package com.mjolnir.yggdrasil.dto;

import com.mjolnir.yggdrasil.entities.CityEntity;

public class CityDeletionResponseDTO {
    private String message;
    private CityEntity city;

    public CityDeletionResponseDTO(String message, CityEntity city) {
        this.message = message;
        this.city = city;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }
}

