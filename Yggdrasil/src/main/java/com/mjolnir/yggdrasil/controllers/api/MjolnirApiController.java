package com.mjolnir.yggdrasil.controllers.api;

import com.mjolnir.yggdrasil.exceptions.InvalidBodyException;
import com.mjolnir.yggdrasil.service.MjolnirApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/Mjolnir/api/keygen")
public class MjolnirApiController {
    private final MjolnirApiService mjolnirApiService;

    @Autowired
    public MjolnirApiController(MjolnirApiService mjolnirApiService) {
        this.mjolnirApiService = mjolnirApiService;
    }

    @GetMapping
    public ResponseEntity<String> generateNewApiKey(@RequestParam String role, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
        if(requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");

        String key = mjolnirApiService.generateApiKey(role).orElseThrow(() -> new InvalidBodyException("Invalid role provided"));

        return ResponseEntity.ok(key);
    }
}
