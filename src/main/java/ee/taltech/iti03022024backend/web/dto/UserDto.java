package ee.taltech.iti03022024backend.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {
    @NotNull(message = "id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "username cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max=255, message = "Username cannot be longer than 255 characters",
            groups = {OnUpdate.class, OnCreate.class})
    private String username;

    @NotNull(message = "email cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max=255, message = "Email cannot be longer than 255 characters",
            groups = {OnUpdate.class, OnCreate.class})
    @Email
    private String email;

    @NotNull(message = "password cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double commonRating;
}
