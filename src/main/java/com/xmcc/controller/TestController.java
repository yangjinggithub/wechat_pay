package com.xmcc.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/hello")
public class TestController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello spring-boot";
    }
}
