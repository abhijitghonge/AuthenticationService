package org.ag.authenticationservice.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.ag.authenticationservice.exceptions.PasswordMismatchException;
import org.ag.authenticationservice.exceptions.UserAlreadyPresentException;
import org.ag.authenticationservice.exceptions.UserNotFoundException;
import org.ag.authenticationservice.models.User;
import org.ag.authenticationservice.repositories.UserRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    public Pair<User, MultiValueMap<String, String>> login(String email, String password) throws UserNotFoundException, PasswordMismatchException {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"+email));

        if(bCrypt.matches(password, user.getPassword())) {
            String message = """
                    {
                       "email": "anurag@scaler.com",
                       "roles": [
                          "instructor",
                          "buddy"
                       ],
                       "expirationDate": "2ndJuly2024"
                    }""";
            byte[] contents = message.getBytes(StandardCharsets.UTF_8);


            Map<String, Object> claims = new HashMap<>();
            claims.put("email", email);
            claims.put("roles", user.getRoles());
            Instant expiryDate = LocalDate.now()
                    .plusMonths(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant();
            claims.put("expirationDate", Date.from(expiryDate));
            claims.put("iat", Date.from(Instant.now()));

            MacAlgorithm algorithm = Jwts.SIG.HS256;
            SecretKey secretKey = algorithm.key().build();

            String token = Jwts.builder().claims(claims).signWith(secretKey).compact();
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, token);
            return new Pair<>(user, headers);
        }else {
            throw new PasswordMismatchException("Password is incorrect! Please retry");
        }
    }
}
