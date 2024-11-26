package ee.taltech.iti03022024backend.security.jwt;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.repository.BlockedJwtRepository;
import ee.taltech.iti03022024backend.security.jwt.props.JwtProperties;
import ee.taltech.iti03022024backend.service.UserService;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtResponse;
import ee.taltech.iti03022024backend.web.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String TOKEN_ID = "tokenId";

    private final JwtProperties jwtProperties;
    private final BlockedJwtRepository blockedJwtRepository;

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final UserMapper userMapper;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(
            final Long userId,
            final String username,
            final Set<Role> roles,
            final UUID tokenId
    ) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .add("roles", resolveRoles(roles))
                .add(TOKEN_ID, tokenId)
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.DAYS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    private List<String> resolveRoles(
            final Set<Role> roles
    ) {
        return roles.stream()
                .map(Enum::name)
                .toList();
    }

    public String createRefreshToken(
            final Long userId,
            final String username,
            final UUID tokenId
    ) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .add(TOKEN_ID, tokenId)
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(
            final String refreshToken
    ) {
        JwtResponse jwtResponse = new JwtResponse();
        if (!isValid(refreshToken)) {
            throw new AccessDeniedException("Invalid refresh token");
        }
        Long userId = getId(refreshToken);
        User user = userMapper.toEntity(userService.getUserById(userId));
        UUID tokenId = UUID.fromString(getTokenId(refreshToken));
        jwtResponse.setAccessToken(
                createAccessToken(userId, user.getUsername(), user.getRoles(), tokenId)
        );
        jwtResponse.setRefreshToken(
                createRefreshToken(userId, user.getUsername(), tokenId)
        );
        return jwtResponse;
    }

    @SneakyThrows
    public boolean isValid(
            final String token
    ) {
        if (blockedJwtRepository.existsByTokenId(UUID.fromString(getTokenId(token)))) {
            return false;
        }
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claims.getPayload()
                .getExpiration()
                .after(new Date());
    }

    private Long getId(
            final String token
    ) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }

    public Authentication getAuthentication(
            final String token
    ) {
        Long userId = getId(token);
        User user = userMapper.toEntity(userService.getUserById(userId));
        return new UsernamePasswordAuthenticationToken(
                user,
                "",
                user.getAuthorities()
        );
    }

    public String getTokenId(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(TOKEN_ID, String.class);
    }
}
