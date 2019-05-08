package com.poseidon.web.rest;
import com.poseidon.service.ProductOrderService;
import com.poseidon.web.rest.errors.BadRequestAlertException;
import com.poseidon.web.rest.util.HeaderUtil;
import com.poseidon.web.rest.util.PaginationUtil;
import com.poseidon.service.dto.ProductOrderDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProductOrder.
 */
@RestController
@RequestMapping("/api")
public class ProductOrderResource {

    private final Logger log = LoggerFactory.getLogger(ProductOrderResource.class);

    private static final String ENTITY_NAME = "productOrder";

    private final ProductOrderService productOrderService;

    public ProductOrderResource(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    /**
     * POST  /product-orders : Create a new productOrder.
     *
     * @param productOrderDTO the productOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productOrderDTO, or with status 400 (Bad Request) if the productOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-orders")
    public ResponseEntity<ProductOrderDTO> createProductOrder(@Valid @RequestBody ProductOrderDTO productOrderDTO) throws URISyntaxException {
        log.debug("REST request to save ProductOrder : {}", productOrderDTO);
        if (productOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new productOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductOrderDTO result = productOrderService.save(productOrderDTO);
        return ResponseEntity.created(new URI("/api/product-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-orders : Updates an existing productOrder.
     *
     * @param productOrderDTO the productOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productOrderDTO,
     * or with status 400 (Bad Request) if the productOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the productOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-orders")
    public ResponseEntity<ProductOrderDTO> updateProductOrder(@Valid @RequestBody ProductOrderDTO productOrderDTO) throws URISyntaxException {
        log.debug("REST request to update ProductOrder : {}", productOrderDTO);
        if (productOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductOrderDTO result = productOrderService.save(productOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-orders : get all the productOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of productOrders in body
     */
    @GetMapping("/product-orders")
    public ResponseEntity<List<ProductOrderDTO>> getAllProductOrders(Pageable pageable) {
        log.debug("REST request to get a page of ProductOrders");
        Page<ProductOrderDTO> page = productOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /product-orders/:id : get the "id" productOrder.
     *
     * @param id the id of the productOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-orders/{id}")
    public ResponseEntity<ProductOrderDTO> getProductOrder(@PathVariable Long id) {
        log.debug("REST request to get ProductOrder : {}", id);
        Optional<ProductOrderDTO> productOrderDTO = productOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productOrderDTO);
    }

    /**
     * DELETE  /product-orders/:id : delete the "id" productOrder.
     *
     * @param id the id of the productOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-orders/{id}")
    public ResponseEntity<Void> deleteProductOrder(@PathVariable Long id) {
        log.debug("REST request to delete ProductOrder : {}", id);
        productOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-orders?query=:query : search for the productOrder corresponding
     * to the query.
     *
     * @param query the query of the productOrder search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/product-orders")
    public ResponseEntity<List<ProductOrderDTO>> searchProductOrders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProductOrders for query {}", query);
        Page<ProductOrderDTO> page = productOrderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/product-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
