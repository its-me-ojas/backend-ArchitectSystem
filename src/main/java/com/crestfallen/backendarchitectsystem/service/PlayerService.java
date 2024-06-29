package com.crestfallen.backendarchitectsystem.service;

import com.crestfallen.backendarchitectsystem.model.Player;
import com.crestfallen.backendarchitectsystem.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Player saveUser(Player player) {
        player.setPassword(encoder.encode(player.getPassword()));
        player.setTotalPoints(0);
        player.setLevel(player.getTotalPoints() / 10);
        player.setCreationDate(new Date());
        player.setLastActiveDate(player.getCreationDate());
        return playerRepository.save(player);
    }
}
