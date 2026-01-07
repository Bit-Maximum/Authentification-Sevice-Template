package plugin.orng.auth.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import plugin.orng.auth.entity.*;

@Repository
public interface RolesRepository extends JpaRepository<RoleEntity, Long>, PagingAndSortingRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByCode(String login);

}
