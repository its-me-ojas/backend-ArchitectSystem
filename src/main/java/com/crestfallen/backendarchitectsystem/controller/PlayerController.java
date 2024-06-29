package com.crestfallen.backendarchitectsystem.controller;

import com.crestfallen.backendarchitectsystem.model.Player;
import com.crestfallen.backendarchitectsystem.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("register")
    public Player register(@RequestBody Player player) {
        return playerService.saveUser(player);
    }

    @PostMapping("login")
    public String login(@RequestBody Player player) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        player.getUsername(),
                        player.getPassword()
                ));
        if (authentication.isAuthenticated())
            return "Success";
        else
            return "Failed";
    }
}
