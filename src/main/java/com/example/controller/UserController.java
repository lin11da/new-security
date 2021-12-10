package com.example.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author chen
 * @version 1.0
 * @date 2021/12/10 15:34
 */
@RestController
public class UserController {

    @Secured("ROLE_abc")
    @PostMapping("/toMain")
    public String lo(){
        return "toMain";
    }
}
