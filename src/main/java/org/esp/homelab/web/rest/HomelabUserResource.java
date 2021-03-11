package org.esp.homelab.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.esp.homelab.service.HomelabUserQueryService;
import org.esp.homelab.service.HomelabUserService;
import org.esp.homelab.service.dto.HomelabUserCriteria;
import org.esp.homelab.service.dto.HomelabUserDTO;
import org.esp.homelab.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link org.esp.homelab.domain.HomelabUser}.
 */
@RestController
@RequestMapping("/api")
public class HomelabUserResource {
    private final Logger log = LoggerFactory.getLogger(HomelabUserResource.class);

    private static final String ENTITY_NAME = "homelabUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HomelabUserService homelabUserService;

    private final HomelabUserQueryService homelabUserQueryService;

    public HomelabUserResource(HomelabUserService homelabUserService, HomelabUserQueryService homelabUserQueryService) {
        this.homelabUserService = homelabUserService;
        this.homelabUserQueryService = homelabUserQueryService;
    }

    /**
     * {@code POST  /homelab-users} : Create a new homelabUser.
     *
     * @param homelabUserDTO the homelabUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new homelabUserDTO, or with status {@code 400 (Bad Request)} if the homelabUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/homelab-users")
    public ResponseEntity<HomelabUserDTO> createHomelabUser(@Valid @RequestBody HomelabUserDTO homelabUserDTO) throws URISyntaxException {
        log.debug("REST request to save HomelabUser : {}", homelabUserDTO);
        if (homelabUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new homelabUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(homelabUserDTO.getUserId())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        HomelabUserDTO result = homelabUserService.save(homelabUserDTO);
        return ResponseEntity
            .created(new URI("/api/homelab-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /homelab-users} : Updates an existing homelabUser.
     *
     * @param homelabUserDTO the homelabUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homelabUserDTO,
     * or with status {@code 400 (Bad Request)} if the homelabUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the homelabUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/homelab-users")
    public ResponseEntity<HomelabUserDTO> updateHomelabUser(@Valid @RequestBody HomelabUserDTO homelabUserDTO) throws URISyntaxException {
        log.debug("REST request to update HomelabUser : {}", homelabUserDTO);
        if (homelabUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HomelabUserDTO result = homelabUserService.save(homelabUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, homelabUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /homelab-users} : get all the homelabUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of homelabUsers in body.
     */
    @GetMapping("/homelab-users")
    public ResponseEntity<List<HomelabUserDTO>> getAllHomelabUsers(HomelabUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HomelabUsers by criteria: {}", criteria);
        Page<HomelabUserDTO> page = homelabUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /homelab-users/count} : count all the homelabUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/homelab-users/count")
    public ResponseEntity<Long> countHomelabUsers(HomelabUserCriteria criteria) {
        log.debug("REST request to count HomelabUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(homelabUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /homelab-users/:id} : get the "id" homelabUser.
     *
     * @param id the id of the homelabUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the homelabUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/homelab-users/{id}")
    public ResponseEntity<HomelabUserDTO> getHomelabUser(@PathVariable Long id) {
        log.debug("REST request to get HomelabUser : {}", id);
        Optional<HomelabUserDTO> homelabUserDTO = homelabUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(homelabUserDTO);
    }

    /**
     * {@code DELETE  /homelab-users/:id} : delete the "id" homelabUser.
     *
     * @param id the id of the homelabUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/homelab-users/{id}")
    public ResponseEntity<Void> deleteHomelabUser(@PathVariable Long id) {
        log.debug("REST request to delete HomelabUser : {}", id);
        homelabUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
