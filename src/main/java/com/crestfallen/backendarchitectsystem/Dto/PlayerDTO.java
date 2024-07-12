package com.crestfallen.backendarchitectsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private String username;
    private String email;
    private Integer age;
    private String password;
}
