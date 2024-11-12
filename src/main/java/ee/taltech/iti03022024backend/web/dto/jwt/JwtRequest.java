package ee.taltech.iti03022024backend.web.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Login request")
public class JwtRequest {
    @NotNull(message = "Username cannot be null")
    @Schema(description = "Username", example = "anton")
    private String username;
    @NotNull(message = "Password cannot be null")
    @Schema(description = "Password", example = "1")
    private String password;
}