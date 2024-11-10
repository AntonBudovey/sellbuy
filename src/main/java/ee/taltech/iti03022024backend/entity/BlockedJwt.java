package ee.taltech.iti03022024backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "blocked_jwt")
@AllArgsConstructor
@NoArgsConstructor
public class BlockedJwt {

    @Id
    @Column(name = "token_id")
    private UUID tokenId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
