package ee.taltech.iti03022024backend.repository;

import ee.taltech.iti03022024backend.entity.BlockedJwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface BlockedJwtRepository extends JpaRepository<BlockedJwt, UUID> {

    @Transactional
    void deleteByExpiresAtBefore(LocalDateTime now);

    boolean existsByTokenId(UUID tokenId);
}
