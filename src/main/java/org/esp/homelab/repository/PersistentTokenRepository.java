package org.esp.homelab.repository;

import java.time.LocalDate;
import java.util.List;
import org.esp.homelab.domain.PersistentToken;
import org.esp.homelab.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link PersistentToken} entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {
    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);
}
