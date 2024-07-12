package com.crestfallen.backendarchitectsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

//    private String timeLeft = getTimeLeftForTheDay();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    @Transient
    public String getTimeLeftForTheDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
        long hoursLeft = ChronoUnit.HOURS.between(now, endOfDay);
        long minutesLeft = ChronoUnit.MINUTES.between(now, endOfDay) % 60;
        long secondsLeft = ChronoUnit.SECONDS.between(now, endOfDay) % 60;

        // Format the time left into a string
        LocalTime timeLeft = LocalTime.of((int) hoursLeft, (int) minutesLeft, (int) secondsLeft);
        return timeLeft.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
