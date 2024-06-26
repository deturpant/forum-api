package ru.deturpant.forum.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.deturpant.forum.api.dto.UserDto;
import ru.deturpant.forum.api.exceptions.BadRequestException;
import ru.deturpant.forum.api.exceptions.NotFoundException;
import ru.deturpant.forum.api.exceptions.UnathorizedException;
import ru.deturpant.forum.api.factories.UserDtoFactory;
import ru.deturpant.forum.api.jwt.JwtAuthenticationResponse;
import ru.deturpant.forum.api.jwt.JwtTokenProvider;
import ru.deturpant.forum.api.requests.LoginRequest;
import ru.deturpant.forum.api.requests.RegRequest;
import ru.deturpant.forum.store.entities.RoleEntity;
import ru.deturpant.forum.store.entities.UserEntity;
import ru.deturpant.forum.store.repositories.UserRepository;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserDtoFactory userDtoFactory;
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    JwtTokenProvider jwtTokenProvider;

    static final String CREATE_USER = "/api/users";
    static final String LOGIN = "/api/login";

    static final String IS_AUTH = "/api/auth";

    @PostMapping(CREATE_USER)
    @Operation(summary = "Registration", description = "Registration")
    public UserDto createUser(
            @RequestBody RegRequest regRequest
            ) {
        String username = regRequest.getUsername();
        String password = regRequest.getPassword();
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new BadRequestException(String.format("User \"%s\" exists", username));
                });
        String hashedPassword = passwordEncoder.encode(password);
        UserEntity user = userRepository.saveAndFlush(
                UserEntity.builder()
                        .createdAt(Instant.now())
                        .password(hashedPassword)
                        .role(RoleEntity.USER)
                        .username(username)
                        .build()
        );
        return userDtoFactory.makeUserDto(user);
    }

    @PostMapping(LOGIN)
    @Operation(summary = "User login", description = "User login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest
    )
    {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        UserEntity currUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (passwordEncoder.matches(password, currUser.getPassword())) {
            String token = jwtTokenProvider.generateToken(username);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        }
        else {
            throw new UnathorizedException("Incorrect password");
        }
    }
}
