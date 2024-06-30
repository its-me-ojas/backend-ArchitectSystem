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

    private Integer strength = 0;
    private Integer intelligence = 0;
    private Integer agility = 0;
    private Integer stamina = 0;
    private Integer discipline = 0;
}
