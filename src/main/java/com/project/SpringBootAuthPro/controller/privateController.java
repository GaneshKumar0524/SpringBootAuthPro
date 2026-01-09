package com.project.SpringBootAuthPro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class privateController {


    @GetMapping("/private")
    public String hello() {
        log.info("Private Hello endpoint accessed");
        return "Private Hello, World!";
    }
}
