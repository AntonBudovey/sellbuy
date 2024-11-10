package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.repository.BlockedJwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlockedJwtService {
    private final BlockedJwtRepository blockedJwtRepository;

    @Scheduled(cron = "0 12 * * * *")
    public void deleteBlockedJwtsWhereExpiresAtAfter() {
        LocalDateTime now = LocalDateTime.now();
        blockedJwtRepository.deleteByExpiresAtBefore(now);
    }
}
