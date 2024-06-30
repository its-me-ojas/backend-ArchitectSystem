package com.crestfallen.backendarchitectsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attributeId;

    @OneToOne(mappedBy = "attributes")
    @JsonBackReference
    private Player player;

//    @OneToOne
//    @JoinColumn(name = "quest_id")
//    private Quest quest;

    private Integer strength;
    private Integer intelligence;
    private Integer agility;
    private Integer stamina;
    private Integer discipline;
}
