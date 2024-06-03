package org.ag.authenticationservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.ag.authenticationservice.enums.SessionState;
import org.ag.authenticationservice.exceptions.PasswordMismatchException;
import org.ag.authenticationservice.exceptions.UserAlreadyPresentException;
import org.ag.authenticationservice.exceptions.UserNotFoundException;
import org.ag.authenticationservice.models.Session;
import org.ag.authenticationservice.models.User;
import org.ag.authenticationservice.repositories.SessionRepository;
import org.ag.authenticationservice.repositories.UserRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
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

    private final SessionRepository sessionRepository;

    private BCryptPasswordEncoder bCrypt;

    @Autowired
    private SecretKey secretKey;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCrypt) {
        this.userRepository = userRepository;
        this.bCrypt = bCrypt;
        this.sessionRepository = sessionRepository;
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
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", email);
            claims.put("roles", user.getRoles());
            Instant expiryDate = LocalDate.now()
                    .plusMonths(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant();
            claims.put("expirationDate", Date.from(expiryDate));
            claims.put("iat", Date.from(Instant.now()));

            String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

            //Storing Session for validation purpose
            Session session = new Session();
            session.setUser(user);
            session.setSessionState(SessionState.ACTIVE);
            session.setToken(token);
            sessionRepository.save(session);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, token);
            return new Pair<>(user, headers);
        }else {
            throw new PasswordMismatchException("Password is incorrect! Please retry");
        }
    }


    public Boolean validateToken(String token, Long userId) {
        Optional<Session> byTokenAndUser = sessionRepository.findByTokenAndUser_id(token, userId);

        if(byTokenAndUser.isEmpty()){
            return false;
        }

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long expiry = (Long)claims.get("expirationDate");
        Long nowInMillis = Instant.now().toEpochMilli();

        if(nowInMillis> expiry){
            System.out.println(expiry);
            System.out.println(nowInMillis);
            System.out.println("Token expired");
            return false;
        }

        String email = userRepository.findById(userId).get().getEmail();
        if(!email.equals(claims.get("email"))){
            System.out.println(email);
            System.out.println(claims.get("email"));
            System.out.println("Emails don't match");
            return false;
        }

        return true;

    }
}
