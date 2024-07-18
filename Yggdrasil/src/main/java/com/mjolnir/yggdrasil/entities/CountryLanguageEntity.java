package com.mjolnir.yggdrasil.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "countrylanguage")
public class CountryLanguageEntity {
    @EmbeddedId
    private CountryLanguageIdEntity id;

    @MapsId("countryCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("''")
    @JoinColumn(name = "CountryCode", nullable = false)
    private CountryEntity countryEntityCode;

    @NotNull
    @ColumnDefault("'F'")
    @Lob
    @Column(name = "IsOfficial", nullable = false)
    private String isOfficial;

    @NotNull
    @ColumnDefault("0.0")
    @Column(name = "Percentage", nullable = false, precision = 4, scale = 1)
    private BigDecimal percentage;

    public CountryLanguageIdEntity getId() {
        return id;
    }

    public void setId(CountryLanguageIdEntity id) {
        this.id = id;
    }

    @JsonBackReference(value = "countryCode")
    public CountryEntity getCountryCode() {
        return countryEntityCode;
    }

    @JsonBackReference(value = "countryCode")
    public void setCountryCode(CountryEntity countryEntityCode) {
        this.countryEntityCode = countryEntityCode;
    }

    public String getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(String isOfficial) {
        this.isOfficial = isOfficial;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public void setLanguage(String language) {
        if (this.id == null) {
            this.id = new CountryLanguageIdEntity();
            this.id.setCountryCode(countryEntityCode.getCode());
        }
        this.id.setLanguage(language);
    }

    public String getLanguage() {
        return this.id.getLanguage();
    }
}