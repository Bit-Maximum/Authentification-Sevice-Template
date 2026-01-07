package plugin.orng.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.*;
import java.util.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tokens")
public class TokenEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NotNull
    @Column(name = "access_jti", nullable = false, unique = true)
    private UUID accessJti;

    @NotNull
    @Column(name = "refresh_jti", nullable = false, unique = true)
    private UUID refreshJti;

    @NotNull
    @Column(name = "refresh_issued_at", nullable = false)
    private Instant issuedAt;

    @NotNull
    @Column(name = "refresh_expired_at")
    private Instant expiredAt;
}
