package com.crestfallen.backendarchitectsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public ResponseEntity<String> sayHello(HttpServletRequest request) {
        return new ResponseEntity<>("hello " + request.getSession().getId(), HttpStatus.OK);
    }

}
