package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.repository.BlockedJwtRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BlockedJwtServiceTest {
    @InjectMocks
    private BlockedJwtService blockedJwtService;
    @Mock
    private BlockedJwtRepository blockedJwtRepository;

    @Test
    void deleteBlockedJwtsWhereExpiresAtAfter() {
        MockitoAnnotations.openMocks(this);
        blockedJwtService.deleteBlockedJwtsWhereExpiresAtAfter();
        verify(blockedJwtRepository, times(1)).deleteByExpiresAtBefore(any());
    }
}