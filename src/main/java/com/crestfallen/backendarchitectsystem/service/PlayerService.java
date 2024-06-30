package com.crestfallen.backendarchitectsystem.service;

import com.crestfallen.backendarchitectsystem.Dto.PlayerDTO;
import com.crestfallen.backendarchitectsystem.exception.Player.PlayerIsAlreadyFriend;
import com.crestfallen.backendarchitectsystem.exception.Player.PlayerNotFoundException;
import com.crestfallen.backendarchitectsystem.exception.Player.SamePasswordException;
import com.crestfallen.backendarchitectsystem.exception.Player.UsernameAlreadyExistsException;
import com.crestfallen.backendarchitectsystem.model.Attribute;
import com.crestfallen.backendarchitectsystem.model.Player;
import com.crestfallen.backendarchitectsystem.model.Quest;
import com.crestfallen.backendarchitectsystem.repository.AttributeRepository;
import com.crestfallen.backendarchitectsystem.repository.PlayerRepository;
import com.crestfallen.backendarchitectsystem.repository.QuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private QuestRepository questRepository;

    public Player saveUser(PlayerDTO playerDTO) {
        if (playerRepository.findByUsername(playerDTO.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("Username " + playerDTO.getUsername() + " already exists");
        }
        Player player = new Player();
        player.setUsername(playerDTO.getUsername());
        player.setPassword(encoder.encode(playerDTO.getPassword()));
        player.setEmail(playerDTO.getEmail());
        player.setAge(playerDTO.getAge());
        player.setCreationDate(new Date());
        player.setLastActiveDate(new Date());

        Attribute attribute = new Attribute();
        player.setAttributes(attribute);
        attributeRepository.save(attribute);

        Quest quest = new Quest();
        player.setQuest(quest);
        questRepository.save(quest);


        return playerRepository.save(player);
    }

    // retrieve a list of players
    public ResponseEntity<List<Player>> getAllPlayers() {
        return new ResponseEntity<>(playerRepository.findAll(), HttpStatus.OK);
    }

    // retrieve  a specific player by id
    public Player getPlayerById(Integer id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()) {
            return player.get();
        } else {
            throw new PlayerNotFoundException("Player with id " + id + " not found");
        }
    }

    // retrieve a specific player by username
    public Player getPlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new PlayerNotFoundException("Player with username " + username + " not found");
        }
        return player;
    }

    // update a player by username
    public Player updatePlayerByUsername(String username, PlayerDTO playerDTO) throws SamePasswordException {
        Player playerToUpdate = playerRepository.findByUsername(username);
        if (playerToUpdate == null) {
            throw new PlayerNotFoundException
                    ("Player with username " + username + " not found");
        }
        if (playerDTO.getUsername() != null) {
            // if player exists whose username has to be changed then
            String newUsername = playerDTO.getUsername();
            if (newUsername != username) {
                // if user already exists with the new username

                if (playerRepository.findByUsername(newUsername) != null)
                    throw new UsernameAlreadyExistsException
                            ("Username " + playerDTO.getUsername() + " already exists");
                playerToUpdate.setUsername(playerDTO.getUsername());
            }
        }
        if (playerDTO.getEmail() != null) {
            if (playerDTO.getEmail() != playerToUpdate.getEmail())
                playerToUpdate.setEmail(playerDTO.getEmail());
        }
        if (playerDTO.getAge() != null) {
            if (playerDTO.getAge() != playerToUpdate.getAge())
                playerToUpdate.setAge(playerDTO.getAge());
        }
        if (playerDTO.getPassword() != null) {
            String oldPassword = encoder.encode(playerDTO.getPassword());
            if (oldPassword != playerToUpdate.getPassword())
                playerToUpdate.setPassword(encoder.encode(playerDTO.getPassword()));
            else
                throw new SamePasswordException("Password is the same as the old password");
        }


        return playerRepository.save(playerToUpdate);
    }

    public void deletePlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new PlayerNotFoundException("Player with username " + username + " not found");
        }
        playerRepository.delete(player);
    }

    public List<Player> getFriendsByUsername(String username) {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new PlayerNotFoundException("Player with username " + username + " not found");
        }
        return player.getFriends();
    }

    public Player addFriendByUsername(String username, String friendUsername)
            throws PlayerIsAlreadyFriend {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new PlayerNotFoundException("Player with username " + username + " not found");
        }
        Player friend = playerRepository.findByUsername(friendUsername);
        if (friend == null) {
            throw new PlayerNotFoundException("Player with username " + friendUsername + " not found");
        }
        if (player.getFriends().contains(friend)) {
            throw new PlayerIsAlreadyFriend("Player with username " + friendUsername + " is already a friend of player with username " + username);
        }
        player.getFriends().add(friend);
        return playerRepository.save(player);
    }

    public Player removeFriendByUsername(String username, String friendUsername)
            throws PlayerIsAlreadyFriend {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new PlayerNotFoundException("Player with username " + username + " not found");
        }
        Player friend = playerRepository.findByUsername(friendUsername);
        if (friend == null) {
            throw new PlayerNotFoundException("Player with username " + friendUsername + " not found");
        }
        if (!player.getFriends().contains(friend)) {
            throw new PlayerIsAlreadyFriend("Player with username " + friendUsername + " is not a friend of player with username " + username);
        }
        player.getFriends().remove(friend);
        return playerRepository.save(player);
    }
}
