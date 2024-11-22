package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.repository.BlockedJwtRepository;
import ee.taltech.iti03022024backend.security.jwt.JwtTokenProvider;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtRequest;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager manager;
    private final JwtTokenProvider provider;


    public JwtResponse login(JwtRequest request) {
        log.info("Try to login into account " + request.getUsername());
        JwtResponse response = new JwtResponse();
        Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = (User) auth.getPrincipal();
        UUID tokeId = UUID.randomUUID();
        response.setAccessToken(provider.createAccessToken(user.getId(), user.getUsername(), user.getRoles(), tokeId));
        response.setRefreshToken(provider.createRefreshToken(user.getId(), user.getUsername(), tokeId));
        return response;
    }

    public JwtResponse refresh(String refreshToken) {
        log.info("Refresh token");
        return provider.refreshUserTokens(refreshToken);
    }
}
