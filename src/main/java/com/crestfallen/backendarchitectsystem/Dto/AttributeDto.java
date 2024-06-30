package com.crestfallen.backendarchitectsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDto {
    private Integer strength;
    private Integer intelligence;
    private Integer agility;
    private Integer stamina;
    private Integer discipline;
}
