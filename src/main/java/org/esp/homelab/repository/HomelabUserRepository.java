package org.esp.homelab.repository;

import org.esp.homelab.domain.HomelabUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HomelabUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HomelabUserRepository extends JpaRepository<HomelabUser, Long>, JpaSpecificationExecutor<HomelabUser> {}
