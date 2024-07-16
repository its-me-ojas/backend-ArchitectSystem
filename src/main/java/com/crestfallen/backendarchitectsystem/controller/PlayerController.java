package com.crestfallen.backendarchitectsystem.controller;

import com.crestfallen.backendarchitectsystem.Dto.PlayerDTO;
import com.crestfallen.backendarchitectsystem.exception.Player.*;
import com.crestfallen.backendarchitectsystem.model.Attribute;
import com.crestfallen.backendarchitectsystem.model.Player;
import com.crestfallen.backendarchitectsystem.service.JwtService;
import com.crestfallen.backendarchitectsystem.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    // /api/v1/player/register
    // register a player
    @PostMapping("register")
    public PlayerDTO register(@RequestBody PlayerDTO playerDTO) {
        return playerService.saveUser(playerDTO);
    }

    // /api/v1/player/login
    // login a player
//    @Transactional(readOnly = true)
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody PlayerDTO player) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            player.getUsername(),
                            player.getPassword()
                    ));
            if (authentication.isAuthenticated())
                return new ResponseEntity<>(jwtService.generateUsername(player.getUsername()), HttpStatus.OK);
        } catch (PlayerNotFoundException | BadCredentialsException e) {
            throw e;
        }
        return new ResponseEntity<>("Password is incorrect", HttpStatus.UNAUTHORIZED);
    }

    // /api/v1/player/players
    // retrieve a list of players
    @GetMapping("players")
    public ResponseEntity<List<Player>> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    // /api/v1/player/{id}
    // retrieve a specific player by id
    @GetMapping("{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Integer id) {
        Player player = playerService.getPlayerById(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    // /api/v1/player/username/{username}
    // retrieve a specific player by username
    @GetMapping("username/{username}")
    public ResponseEntity<Player> getPlayerByUsername(@PathVariable String username) {
        Player player = playerService.getPlayerByUsername(username);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    // /api/v1/player/username/{username}
    // update a specific player by username
    @PutMapping("username/{username}")
    public ResponseEntity<Player> updatePlayerByUsername(
            @PathVariable String username, @RequestBody PlayerDTO playerDTO,
            @AuthenticationPrincipal UserDetails userDetails)
            throws SamePasswordException, UnauthorisedPlayerException {
        String tokenUsername = userDetails.getUsername();
        System.out.println("Token username: " + tokenUsername);

        if (!username.equals(tokenUsername)) {
            throw new UnauthorisedPlayerException("Unauthorised to update player with username " + username);
        }


        Player updatedPlayer = playerService.updatePlayerByUsername(username, playerDTO);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    // /api/v1/player/username/{username}
    // delete a specific player by username
    @DeleteMapping("username/{username}")
    public ResponseEntity<String> deletePlayerByUsername(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails)
            throws UnauthorisedPlayerException {
        String tokenUsername = userDetails.getUsername();
        System.out.println("Token username: " + tokenUsername);
        if (!username.equals(tokenUsername)) {
            throw new UnauthorisedPlayerException("Unauthorised to delete player with username " + username);
        }
        playerService.deletePlayerByUsername(username);
        return new ResponseEntity<>("Player Deleted", HttpStatus.OK);

    }

    // /api/v1/player/{username}/attributes
    // retrieve attributes of a specific player by username
    @GetMapping("{username}/attributes")
    public ResponseEntity<Attribute> getAttributesByUsername(@PathVariable String username) {
        Player player = playerService.getPlayerByUsername(username);
        return new ResponseEntity<>(player.getAttributes(), HttpStatus.OK);
    }

    // /api/v1/player/username/{username}/friends
    // retrieve a list of friends of a player by username
    @GetMapping("username/{username}/friends")
    public ResponseEntity<List<Player>> getFriendsByUsername(@PathVariable String username) {
        List<Player> friends = playerService.getFriendsByUsername(username);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    // /api/v1/player/{username}/add-friend/{friendUsername}
    // add a friend to a specific player by username
    @PostMapping("{username}/add-friend/{friendUsername}")
    public ResponseEntity<List<Player>> addFriendByUsername(
            @PathVariable String username, @PathVariable String friendUsername,
            @AuthenticationPrincipal UserDetails userDetails)
            throws UnauthorisedPlayerException, PlayerIsAlreadyFriend {
        String tokenUsername = userDetails.getUsername();
        System.out.println("Token username: " + tokenUsername);
        if (!username.equals(tokenUsername)) {
            throw new UnauthorisedPlayerException("Unauthorised to add friend to player with username " + username);
        }
        Player player = playerService.addFriendByUsername(username, friendUsername);
        return new ResponseEntity<>(player.getFriends(), HttpStatus.OK);

    }

    // /api/v1/player/{username}/remove-friend/{friendUsername}
    // remove a friend from a specific player by username
    @DeleteMapping("{username}/remove-friend/{friendUsername}")
    public ResponseEntity<List<Player>> removeFriendByUsername(
            @PathVariable String username, @PathVariable String friendUsername,
            @AuthenticationPrincipal UserDetails userDetails)
            throws UnauthorisedPlayerException, PlayerIsAlreadyFriend {
        String tokenUsername = userDetails.getUsername();
        System.out.println("Token username: " + tokenUsername);
        if (!username.equals(tokenUsername)) {
            throw new UnauthorisedPlayerException("Unauthorised to remove friend from player with username " + username);
        }
        Player player = playerService.removeFriendByUsername(username, friendUsername);
        return new ResponseEntity<>(player.getFriends(), HttpStatus.OK);
    }

//    DELETE /players/{id}/friends/{friendId}: Remove a friend from a specific player.

    // Exception handling

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<String> handleSamePasswordException(SamePasswordException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorisedPlayerException.class)
    public ResponseEntity<String> handleUnauthorisedPlayerException(UnauthorisedPlayerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PlayerIsAlreadyFriend.class)
    public ResponseEntity<String> handlePlayerIsAlreadyFriend(PlayerIsAlreadyFriend e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        return new ResponseEntity<>("Bad Credentials", HttpStatus.UNAUTHORIZED);
    }
}
