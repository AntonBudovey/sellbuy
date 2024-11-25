package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.service.AuthService;
import ee.taltech.iti03022024backend.service.UserService;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtRequest;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtResponse;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "http://frontend:3000")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Auth controller", description = "API for authentication")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @Operation(summary = "create new user")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto dto) {
        User user = userService.createUser(userMapper.toEntity(dto));
        return userMapper.toDto(user);
    }

    @PostMapping("/login")
    @Operation(summary = "login as existing user and get access and refresh token")
    public JwtResponse login(@Validated @RequestBody JwtRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "refresh my refresh and access token")
    public JwtResponse refreshToken(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @PostMapping("/logout")
    @Operation(summary = "set your token in header into blocked tokens")
    public void logout() {
        log.info("User logged out");
    }

    @GetMapping("/profile")
    public UserDto retrieveUser(Authentication authentication) {
        return userMapper.toDto((User) authentication.getPrincipal());
    }
}
