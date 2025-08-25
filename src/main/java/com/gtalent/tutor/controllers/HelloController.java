package com.gtalent.tutor.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String getHello () {
        return "Hello, World";
    }

    @GetMapping("/hello2")
    public int getHello2 () {
        return 9988776;
    }

    @GetMapping("/hi")
    public String getHi () {
        return "hi everyone!";
    }

    @PostMapping("/hello")
    public String postHello() {
        return "Hello, this is post";
    }

    @PutMapping("/hello")
    public String putHello() {
        return "put put put.";
    }
}
