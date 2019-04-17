package com.poseidon.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.poseidon.domain.Ship;
import com.poseidon.domain.*; // for static metamodels
import com.poseidon.repository.ShipRepository;
import com.poseidon.service.dto.ShipCriteria;

/**
 * Service for executing complex queries for Ship entities in the database.
 * The main input is a {@link ShipCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ship} or a {@link Page} of {@link Ship} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipQueryService extends QueryService<Ship> {

    private final Logger log = LoggerFactory.getLogger(ShipQueryService.class);

    private final ShipRepository shipRepository;

    public ShipQueryService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    /**
     * Return a {@link List} of {@link Ship} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ship> findByCriteria(ShipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ship> specification = createSpecification(criteria);
        return shipRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ship} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ship> findByCriteria(ShipCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ship> specification = createSpecification(criteria);
        return shipRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ship> specification = createSpecification(criteria);
        return shipRepository.count(specification);
    }

    /**
     * Function to convert ShipCriteria to a {@link Specification}
     */
    private Specification<Ship> createSpecification(ShipCriteria criteria) {
        Specification<Ship> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Ship_.id));
            }
            if (criteria.getShipId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShipId(), Ship_.shipId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Ship_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Ship_.lastName));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Ship_.gender));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Ship_.phone));
            }
            if (criteria.getAddressLine1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine1(), Ship_.addressLine1));
            }
            if (criteria.getAddressLine2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine2(), Ship_.addressLine2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Ship_.city));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Ship_.country));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Ship_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(Ship_.orders, JoinType.LEFT).get(ProductOrder_.id)));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompanyId(),
                    root -> root.join(Ship_.company, JoinType.LEFT).get(Company_.id)));
            }
        }
        return specification;
    }
}
