package com.crestfallen.backendarchitectsystem.repository;

import com.crestfallen.backendarchitectsystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
