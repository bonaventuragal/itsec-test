package com.example.itsec_test.controller;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Tag(name = "Root")
public class RootController {
    @GetMapping("/")
    public String root() {
        return "OK";
    }

}
