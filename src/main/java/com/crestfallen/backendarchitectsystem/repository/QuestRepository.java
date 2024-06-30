package com.crestfallen.backendarchitectsystem.repository;

import com.crestfallen.backendarchitectsystem.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Integer> {
}
