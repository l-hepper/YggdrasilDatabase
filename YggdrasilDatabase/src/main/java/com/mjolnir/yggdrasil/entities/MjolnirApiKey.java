package com.mjolnir.yggdrasil.entities;

import jakarta.persistence.*;

@Entity
public class MjolnirApiKey {
    private static final String[] validRoles = {"READ_ONLY", "FULL_ACCESS"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String apiKey;
    private String role;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
