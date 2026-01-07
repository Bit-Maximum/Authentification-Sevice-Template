package plugin.orng.auth.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import plugin.orng.auth.entity.*;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String login);

}
