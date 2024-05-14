package org.ag.authenticationservice.controllers;

import org.ag.authenticationservice.dtos.*;
import org.ag.authenticationservice.exceptions.UserAlreadyPresentException;
import org.ag.authenticationservice.models.User;
import org.ag.authenticationservice.services.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws UserAlreadyPresentException {

        User user = authService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
        return ResponseEntity.ok(getUserDto(user));


    }

    private SignUpResponseDto getUserDto(User user) {
        return SignUpResponseDto.builder()
                .roles(user.getRoles())
                .email(user.getEmail())
                .status(ResponseStatus.SUCCESS)
                .failureMessage("")
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<SignUpResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return null;
    }

    @PostMapping("/signout")
    public ResponseEntity<SignUpResponseDto> signOut(@RequestBody LogoutRequestDto logoutRequestDto) {
        return null;
    }
}
