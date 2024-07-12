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

}
