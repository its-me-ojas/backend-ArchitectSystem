package com.crestfallen.backendarchitectsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

//    @Lob
//    @JsonIgnore
//    private byte[] avatar;

    private String username;
    private String password;
    private String email;
    private Integer age;
    private Date creationDate;
    private Date lastActiveDate;
    private Integer totalPoints = 0;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attribute_id")
    @JsonManagedReference
    private Attribute attributes;

    private Integer level = 1;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "player_friends",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Player> friends;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quest_id")
    @JsonManagedReference
    private Quest quest;

    @Transient
    public Duration getTimeSinceLastLogin() {
        if (lastActiveDate == null) {
            return Duration.ZERO;
        }
        LocalDateTime lastLogin = new Timestamp(lastActiveDate.getTime()).toLocalDateTime();
        return Duration.between(lastLogin, LocalDateTime.now());
    }
}
