package com.mjolnir.yggdrasil.exceptions;

public class UpdateFailedException extends RuntimeException {
    public UpdateFailedException(String message) {
        super(message);
    }
}
