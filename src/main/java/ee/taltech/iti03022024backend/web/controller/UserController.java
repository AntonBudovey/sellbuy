package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.service.UserService;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User controller", description = "User API")
public class UserController {
    private final UserService userService;


    @PutMapping()
    @Operation(summary = "update user(can user himself and admin)")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#dto.id)")
    public UserDto updateUser(@Validated(OnUpdate.class) @RequestBody UserDto dto) {
        return userService.updateUser(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get user by id")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserByIdWithProducts(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete user by id(can user himself and admin)")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }


}
