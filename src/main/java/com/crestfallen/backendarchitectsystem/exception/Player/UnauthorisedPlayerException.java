package com.crestfallen.backendarchitectsystem.exception.Player;

public class UnauthorisedPlayerException extends RuntimeException {
    public UnauthorisedPlayerException(String message) {
        super(message);
    }
}
