package com.mjolnir.yggdrasil.exceptions;

public class ResponseStatusException extends RuntimeException {
    public ResponseStatusException(String message) {
        super(message);
    }
}
