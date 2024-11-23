package ee.taltech.iti03022024backend.security;

import ee.taltech.iti03022024backend.entity.BlockedJwt;
import ee.taltech.iti03022024backend.repository.BlockedJwtRepository;
import ee.taltech.iti03022024backend.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class LogoutJwtTokenFilter extends OncePerRequestFilter {
    private final BlockedJwtRepository blockedJwtRepository;
    private final JwtTokenProvider provider;
    private final RequestMatcher matcher = new AntPathRequestMatcher("/api/v1/auth/logout", HttpMethod.POST.name());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (matcher.matches(request)) {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                if (provider.isValid(token)) {
                    blockedJwtRepository.save(new BlockedJwt(UUID.fromString(provider.getTokenId(token))
                            , LocalDateTime.now().plusDays(1)));
                }
                var context = SecurityContextHolder.createEmptyContext();
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);

    }
}
