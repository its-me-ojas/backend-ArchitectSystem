package com.crestfallen.backendarchitectsystem.exception.Player;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
