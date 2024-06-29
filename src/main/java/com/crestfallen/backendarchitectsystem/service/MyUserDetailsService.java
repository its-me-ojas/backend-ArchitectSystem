package com.crestfallen.backendarchitectsystem.service;

import com.crestfallen.backendarchitectsystem.model.Player;
import com.crestfallen.backendarchitectsystem.model.PlayerPrincipal;
import com.crestfallen.backendarchitectsystem.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            System.out.println("User not found 404");
            throw new UsernameNotFoundException("User 404");
        }
        return new PlayerPrincipal(player);
    }
}
