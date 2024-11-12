package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.service.UserService;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import ee.taltech.iti03022024backend.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Validated
@Tag(name = "User controller", description = "User API")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


    @PutMapping
    @Operation(summary = "update user(can user himself and admin)")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#dto.id)")
    public UserDto updateUser(@Validated(OnUpdate.class) @RequestBody UserDto dto) {
        User user = userService.updateUser(userMapper.toEntity(dto));
        return userMapper.toDto(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get user by id")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.getUserByIdWithProducts(id);
        UserDto userDto = userMapper.toDto(user);
        userDto.setCommonRating(userService.getCommonRatingForUser(id));
        return userDto;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete user by id(can user himself and admin)")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }




}
