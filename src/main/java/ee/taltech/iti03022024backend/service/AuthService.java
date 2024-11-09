package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.security.jwt.JwtTokenProvider;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtRequest;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager manager;
    private final JwtTokenProvider provider;

    public JwtResponse login(JwtRequest request) {
        JwtResponse response = new JwtResponse();
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userService.getUserByUsername(request.getUsername());
        response.setAccessToken(provider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        response.setRefreshToken(provider.createRefreshToken(user.getId(), user.getUsername()));
        return response;
    }

    public JwtResponse refresh(String refreshToken) {
        return provider.refreshUserTokens(refreshToken);
    }
}
