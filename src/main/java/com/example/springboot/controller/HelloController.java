package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chumeng
 * @date 2022/10/10
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "Hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "Hello user";
    }
}
