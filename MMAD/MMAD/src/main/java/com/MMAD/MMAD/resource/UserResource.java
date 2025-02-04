package com.MMAD.MMAD.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.service.UserService;


@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService){
        this.userService = userService;
    }
}
