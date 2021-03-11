package org.esp.homelab.service;

import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.esp.homelab.domain.*; // for static metamodels
import org.esp.homelab.domain.HomelabUser;
import org.esp.homelab.repository.HomelabUserRepository;
import org.esp.homelab.service.dto.HomelabUserCriteria;
import org.esp.homelab.service.dto.HomelabUserDTO;
import org.esp.homelab.service.mapper.HomelabUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link HomelabUser} entities in the database.
 * The main input is a {@link HomelabUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HomelabUserDTO} or a {@link Page} of {@link HomelabUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HomelabUserQueryService extends QueryService<HomelabUser> {
    private final Logger log = LoggerFactory.getLogger(HomelabUserQueryService.class);

    private final HomelabUserRepository homelabUserRepository;

    private final HomelabUserMapper homelabUserMapper;

    public HomelabUserQueryService(HomelabUserRepository homelabUserRepository, HomelabUserMapper homelabUserMapper) {
        this.homelabUserRepository = homelabUserRepository;
        this.homelabUserMapper = homelabUserMapper;
    }

    /**
     * Return a {@link List} of {@link HomelabUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HomelabUserDTO> findByCriteria(HomelabUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HomelabUser> specification = createSpecification(criteria);
        return homelabUserMapper.toDto(homelabUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HomelabUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HomelabUserDTO> findByCriteria(HomelabUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HomelabUser> specification = createSpecification(criteria);
        return homelabUserRepository.findAll(specification, page).map(homelabUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HomelabUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HomelabUser> specification = createSpecification(criteria);
        return homelabUserRepository.count(specification);
    }

    /**
     * Function to convert {@link HomelabUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HomelabUser> createSpecification(HomelabUserCriteria criteria) {
        Specification<HomelabUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HomelabUser_.id));
            }
            if (criteria.getNumCNI() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumCNI(), HomelabUser_.numCNI));
            }
            if (criteria.getDateDeNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDeNaissance(), HomelabUser_.dateDeNaissance));
            }
            if (criteria.getAddressLine1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine1(), HomelabUser_.addressLine1));
            }
            if (criteria.getAddressLine2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine2(), HomelabUser_.addressLine2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), HomelabUser_.city));
            }
            if (criteria.getPays() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPays(), HomelabUser_.pays));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), HomelabUser_.phone));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(HomelabUser_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
