package com.crestfallen.backendarchitectsystem.repository;

import com.crestfallen.backendarchitectsystem.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findByUsername(String username);
}
