package org.ag.authenticationservice.security.services;


import org.ag.authenticationservice.models.User;
import org.ag.authenticationservice.repositories.UserRepository;
import org.ag.authenticationservice.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User Not found"));
        return new CustomUserDetails(user);
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }
}
