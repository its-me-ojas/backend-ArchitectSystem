package com.crestfallen.backendarchitectsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    private Boolean isCompleted;
    private String description;
    private String category;

    @ManyToOne
    @JoinColumn(name = "quest_id")
    private Quest quest;
}
