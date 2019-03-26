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

import com.poseidon.domain.ProductOrder;
import com.poseidon.domain.*; // for static metamodels
import com.poseidon.repository.ProductOrderRepository;
import com.poseidon.service.dto.ProductOrderCriteria;

/**
 * Service for executing complex queries for ProductOrder entities in the database.
 * The main input is a {@link ProductOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductOrder} or a {@link Page} of {@link ProductOrder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductOrderQueryService extends QueryService<ProductOrder> {

    private final Logger log = LoggerFactory.getLogger(ProductOrderQueryService.class);

    private final ProductOrderRepository productOrderRepository;

    public ProductOrderQueryService(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    /**
     * Return a {@link List} of {@link ProductOrder} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductOrder> findByCriteria(ProductOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductOrder> specification = createSpecification(criteria);
        return productOrderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ProductOrder} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOrder> findByCriteria(ProductOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductOrder> specification = createSpecification(criteria);
        return productOrderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductOrder> specification = createSpecification(criteria);
        return productOrderRepository.count(specification);
    }

    /**
     * Function to convert ProductOrderCriteria to a {@link Specification}
     */
    private Specification<ProductOrder> createSpecification(ProductOrderCriteria criteria) {
        Specification<ProductOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProductOrder_.id));
            }
            if (criteria.getPlacedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlacedDate(), ProductOrder_.placedDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ProductOrder_.status));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), ProductOrder_.code));
            }
            if (criteria.getOrderItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderItemId(),
                    root -> root.join(ProductOrder_.orderItems, JoinType.LEFT).get(OrderItem_.id)));
            }
            if (criteria.getShipId() != null) {
                specification = specification.and(buildSpecification(criteria.getShipId(),
                    root -> root.join(ProductOrder_.ship, JoinType.LEFT).get(Ship_.id)));
            }
        }
        return specification;
    }
}
