package com.crestfallen.backendarchitectsystem.repository;

import com.crestfallen.backendarchitectsystem.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.isCompleted = false")
    void resetAllTasks();
}
