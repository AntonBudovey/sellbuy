package ee.taltech.iti03022024backend.web.dto.jwt;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
