package com.xmcc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class TestController {

    @RequestMapping("/world")
    public String hello(){
        return "hello spring-boot";
    }
}
