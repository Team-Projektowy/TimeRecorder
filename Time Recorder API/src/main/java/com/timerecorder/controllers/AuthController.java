package com.timerecorder.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.timerecorder.models.TokenAPI;
import com.timerecorder.models.User;
import com.timerecorder.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Date;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Environment environment) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = environment;
    }

    @PostMapping(path="/register")
    public User register (@Valid @RequestBody User user) {
        if (userRepository.findFirstByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with provided email already exists");
        }

        String hash = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        return userRepository.save(user);
    }

    @PostMapping(path="/login")
    public TokenAPI login (@RequestBody ObjectNode json) {
        String email = json.get("email").textValue();
        String password = json.get("password").textValue();

        User user = userRepository.findFirstByEmail(email).orElse(null);

        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect credentials");
        }

        long currentTimeMilis = System.currentTimeMillis();
        String token = Jwts
                .builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("isAdmin", user.isAdmin())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("position", user.getPosition())
                .claim("hoursAWeek", user.getHoursAWeek())
                .setIssuedAt(new Date(currentTimeMilis))
                .setExpiration(new Date(currentTimeMilis + 7200000))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("jwt.secret"))
                .compact();

        return new TokenAPI(token, user);
    }
}
