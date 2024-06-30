package com.crestfallen.backendarchitectsystem.repository;

import com.crestfallen.backendarchitectsystem.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
}
