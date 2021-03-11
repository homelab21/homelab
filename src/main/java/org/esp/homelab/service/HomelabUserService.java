package org.esp.homelab.service;

import java.util.Optional;
import org.esp.homelab.service.dto.HomelabUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.esp.homelab.domain.HomelabUser}.
 */
public interface HomelabUserService {
    /**
     * Save a homelabUser.
     *
     * @param homelabUserDTO the entity to save.
     * @return the persisted entity.
     */
    HomelabUserDTO save(HomelabUserDTO homelabUserDTO);

    /**
     * Get all the homelabUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HomelabUserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" homelabUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HomelabUserDTO> findOne(Long id);

    /**
     * Delete the "id" homelabUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
