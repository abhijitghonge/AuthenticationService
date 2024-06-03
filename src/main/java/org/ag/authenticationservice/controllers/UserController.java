package org.ag.authenticationservice.controllers;

import org.ag.authenticationservice.dtos.UserDto;
import org.ag.authenticationservice.models.User;
import org.ag.authenticationservice.security.services.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        System.out.println(id);
        User user = userService.getUser(id).orElseThrow(()->new UsernameNotFoundException("User not found!:"+id));

        return UserDto.builder()
                .email(user.getEmail())
                .build();

    }
}
