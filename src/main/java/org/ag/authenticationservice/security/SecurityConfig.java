package org.ag.authenticationservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        return http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->request.anyRequest().permitAll())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder getBcrypt(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey getSecretKey(){
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        return algorithm.key().build();
    }
}
