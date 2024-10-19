package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.service.UserService;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import ee.taltech.iti03022024backend.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.register(userDto);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(new UserDto());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        UserDto loggedInUser = userService.login(userDto.getUsername(), userDto.getPassword());
        return ResponseEntity.ok(loggedInUser);
    }
//    @PostMapping
//    public UserDto createUser(@Validated(OnCreate.class) @RequestBody UserDto dto) {
//        if (!dto.getConfirmPassword().equals(dto.getPassword())) {
//            throw new IllegalArgumentException("Passwords do not match");
//        }
//        User user = userService.createUser(userMapper.toEntity(dto));
//        return userMapper.toDto(user);
//    }

    @PutMapping
    public UserDto updateUser(@Validated(OnUpdate.class) @RequestBody UserDto dto) {
        User user = userService.updateUser(userMapper.toEntity(dto));
        return userMapper.toDto(user);
    }

//    @GetMapping("/{id}")
//    public UserDto getUserById(@PathVariable Long id) {
//        User user = userService.getUserById(id);
//        UserDto userDto = userMapper.toDto(user);
//        userDto.setCommonRating(userService.getCommonRatingForUser(id));
//        return userDto;
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
