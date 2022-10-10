package com.example.springboot.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.TimerTask;

/**
 * @author chumeng
 * @date 2022/10/10
 */
public class Main {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("chumeng"));

    }
}
