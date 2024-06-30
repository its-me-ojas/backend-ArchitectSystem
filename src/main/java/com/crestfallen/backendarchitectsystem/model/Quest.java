package com.crestfallen.backendarchitectsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questId;

    @OneToOne(mappedBy = "quest")
    @JsonBackReference
    private Player player;

//    @OneToOne(mappedBy = "quest")
//    private Attribute attribute;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Task> tasks;

    private LocalDateTime timeLeft;
}
