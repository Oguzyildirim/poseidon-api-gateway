package com.poseidon.service;

import com.poseidon.domain.ProductOrder;
import com.poseidon.repository.ProductOrderRepository;
import com.poseidon.repository.search.ProductOrderSearchRepository;
import com.poseidon.service.dto.ProductOrderDTO;
import com.poseidon.service.mapper.ProductOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProductOrder.
 */
@Service
@Transactional
public class ProductOrderService {

    private final Logger log = LoggerFactory.getLogger(ProductOrderService.class);

    private final ProductOrderRepository productOrderRepository;

    private final ProductOrderMapper productOrderMapper;

    private final ProductOrderSearchRepository productOrderSearchRepository;

    public ProductOrderService(ProductOrderRepository productOrderRepository, ProductOrderMapper productOrderMapper, ProductOrderSearchRepository productOrderSearchRepository) {
        this.productOrderRepository = productOrderRepository;
        this.productOrderMapper = productOrderMapper;
        this.productOrderSearchRepository = productOrderSearchRepository;
    }

    /**
     * Save a productOrder.
     *
     * @param productOrderDTO the entity to save
     * @return the persisted entity
     */
    public ProductOrderDTO save(ProductOrderDTO productOrderDTO) {
        log.debug("Request to save ProductOrder : {}", productOrderDTO);
        ProductOrder productOrder = productOrderMapper.toEntity(productOrderDTO);
        productOrder = productOrderRepository.save(productOrder);
        ProductOrderDTO result = productOrderMapper.toDto(productOrder);
        productOrderSearchRepository.save(productOrder);
        return result;
    }

    /**
     * Get all the productOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductOrders");
        return productOrderRepository.findAll(pageable)
            .map(productOrderMapper::toDto);
    }


    /**
     * Get one productOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProductOrderDTO> findOne(Long id) {
        log.debug("Request to get ProductOrder : {}", id);
        return productOrderRepository.findById(id)
            .map(productOrderMapper::toDto);
    }

    /**
     * Delete the productOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductOrder : {}", id);
        productOrderRepository.deleteById(id);
        productOrderSearchRepository.deleteById(id);
    }

    /**
     * Search for the productOrder corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductOrderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductOrders for query {}", query);
        return productOrderSearchRepository.search(queryStringQuery(query), pageable)
            .map(productOrderMapper::toDto);
    }
}
