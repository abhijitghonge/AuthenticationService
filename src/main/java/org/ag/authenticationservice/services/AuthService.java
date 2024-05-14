package org.ag.authenticationservice.services;

import org.ag.authenticationservice.exceptions.UserAlreadyPresentException;
import org.ag.authenticationservice.models.User;
import org.ag.authenticationservice.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private BCryptPasswordEncoder bCrypt;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCrypt) {
        this.userRepository = userRepository;
        this.bCrypt = bCrypt;
    }

    public User signUp(String email, String password) throws UserAlreadyPresentException {
        Optional<User> byEmail = userRepository.findByEmail(email);

        if (byEmail.isPresent()) {
            throw new UserAlreadyPresentException("Email already exist! :" + email);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCrypt.encode(password));

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        return null;
    }
}
