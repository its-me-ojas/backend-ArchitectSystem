package com.crestfallen.backendarchitectsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String username;
    private String password;
    private String email;
    private Integer age;
    private Date creationDate;
    private Date lastActiveDate;
    private Integer totalPoints;

    @OneToOne
    @JoinColumn(name = "attribute_id")
    @JsonManagedReference
    private Attribute attributes;

    private Integer level;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "player_friends",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Player> friends;

    @OneToOne
    @JoinColumn(name = "quest_id")
    @JsonManagedReference
    private Quest quest;
}
