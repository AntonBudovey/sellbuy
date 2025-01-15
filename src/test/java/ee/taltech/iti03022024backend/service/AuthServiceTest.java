package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.security.jwt.JwtTokenProvider;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtRequest;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManager manager;
    @Mock
    private JwtTokenProvider provider;
    private JwtRequest request;
    private JwtResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new JwtRequest("test", "test");
        response = new JwtResponse("test", "test");

    }

    @Test
    void login() {
        User user = new User(1L, "test", "test", "test", Set.of(Role.ROLE_USER), null, null);
        Mockito.when(manager.authenticate(any(Authentication.class))).thenReturn(new UsernamePasswordAuthenticationToken(user, null));
        Mockito.when(provider.createAccessToken(any(Long.class), any(String.class), ArgumentMatchers.<Set<Role>>any(), any(UUID.class))).thenReturn("test");
        Mockito.when(provider.createRefreshToken(any(Long.class), any(String.class), any(UUID.class))).thenReturn("test");
        JwtResponse jwtresponse = authService.login(request);
        assertEquals(response.getAccessToken(), jwtresponse.getAccessToken());
        assertEquals(response.getRefreshToken(), jwtresponse.getRefreshToken());
    }

    @Test
    void refresh() {
        Mockito.when(provider.refreshUserTokens("test")).thenReturn(response);
        JwtResponse jwtresponse = authService.refresh("test");
        assertEquals(response, jwtresponse);
    }
}