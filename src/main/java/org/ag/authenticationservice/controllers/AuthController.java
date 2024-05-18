package org.ag.authenticationservice.controllers;

import org.ag.authenticationservice.dtos.*;
import org.ag.authenticationservice.exceptions.PasswordMismatchException;
import org.ag.authenticationservice.exceptions.UserAlreadyPresentException;
import org.ag.authenticationservice.exceptions.UserNotFoundException;
import org.ag.authenticationservice.models.User;
import org.ag.authenticationservice.services.AuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws UserNotFoundException, PasswordMismatchException {
        Pair<User, MultiValueMap<String, String>> loginInfo = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        LoginResponseDto loginResponseDto  = LoginResponseDto.builder()
                .email(loginInfo.a.getEmail())
                .status(ResponseStatus.SUCCESS)
                .build();

        return new ResponseEntity<>(loginResponseDto, loginInfo.b, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody ValidateTokenRequestDto validateTokenRequestDto)  {
        return ResponseEntity.ok(authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId()));
    }

    @PostMapping("/signout")
    public ResponseEntity<SignUpResponseDto> signOut(@RequestBody LogoutRequestDto logoutRequestDto) {
        return null;
    }
}
