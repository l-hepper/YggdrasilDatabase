package com.mjolnir.yggdrasil.controllers;

import com.mjolnir.yggdrasil.exceptions.InvalidBodyException;
import com.mjolnir.yggdrasil.service.MjolnirApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/Mjolnir/keygen")
public class MjolnirApiController {
    private final MjolnirApiService mjolnirApiService;

    @Autowired
    public MjolnirApiController(MjolnirApiService mjolnirApiService) {
        this.mjolnirApiService = mjolnirApiService;
    }

    @GetMapping
    public ResponseEntity<String> getLanguageById(@RequestParam String role, HttpServletRequest request) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(request.getHeader("MJOLNIR-API-KEY"));
        if(requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");

        String key = mjolnirApiService.generateApiKey(role).orElseThrow(() -> new InvalidBodyException("Invalid role provided"));

        return ResponseEntity.ok(key);
    }
}
