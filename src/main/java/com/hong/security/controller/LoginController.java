package com.hong.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanghong
 * @date 2020/05/11 21:22
 **/
@RestController
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "Hello ,spring security!";
    }

}
