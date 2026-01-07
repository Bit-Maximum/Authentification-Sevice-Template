package plugin.orng.auth.repository;

import java.time.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.orng.auth.entity.*;

@Repository
public interface TokensRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByRefreshJti(UUID jti);

    @Query(
            """
            SELECT t FROM tokens t inner join users u
            on t.user.id = u.id
            where t.user.id = :userId and t.expiredAt < :now
            """)
    List<TokenEntity> findAllAccessTokenByUser(
            @Param("userId") Long userId, @Param("now") Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            """
        update tokens t
        set t.expiredAt = :now
        where t.refreshJti = :jti
    """)
    int softDeleteByRefreshJti(@Param("jti") UUID jti, @Param("now") Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            """
        update tokens t
        set t.expiredAt = :now
        where t.accessJti = :jti
    """)
    int softDeleteByAccessJti(@Param("jti") UUID jti, @Param("now") Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            """
        update tokens t
        set t.expiredAt = :now
        where t.user.id = :userId
    """)
    int softDeleteByUserId(@Param("userId") Long userId, @Param("now") Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            """
        delete from tokens t
        where t.expiredAt < :now
    """)
    int deleteAllExpired(@Param("now") Instant now);
}
