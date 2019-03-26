package com.poseidon.web.rest;
import com.poseidon.domain.OrderItem;
import com.poseidon.service.OrderItemService;
import com.poseidon.web.rest.errors.BadRequestAlertException;
import com.poseidon.web.rest.util.HeaderUtil;
import com.poseidon.web.rest.util.PaginationUtil;
import com.poseidon.service.dto.OrderItemCriteria;
import com.poseidon.service.OrderItemQueryService;
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

/**
 * REST controller for managing OrderItem.
 */
@RestController
@RequestMapping("/api")
public class OrderItemResource {

    private final Logger log = LoggerFactory.getLogger(OrderItemResource.class);

    private static final String ENTITY_NAME = "orderItem";

    private final OrderItemService orderItemService;

    private final OrderItemQueryService orderItemQueryService;

    public OrderItemResource(OrderItemService orderItemService, OrderItemQueryService orderItemQueryService) {
        this.orderItemService = orderItemService;
        this.orderItemQueryService = orderItemQueryService;
    }

    /**
     * POST  /order-items : Create a new orderItem.
     *
     * @param orderItem the orderItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderItem, or with status 400 (Bad Request) if the orderItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-items")
    public ResponseEntity<OrderItem> createOrderItem(@Valid @RequestBody OrderItem orderItem) throws URISyntaxException {
        log.debug("REST request to save OrderItem : {}", orderItem);
        if (orderItem.getId() != null) {
            throw new BadRequestAlertException("A new orderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderItem result = orderItemService.save(orderItem);
        return ResponseEntity.created(new URI("/api/order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-items : Updates an existing orderItem.
     *
     * @param orderItem the orderItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderItem,
     * or with status 400 (Bad Request) if the orderItem is not valid,
     * or with status 500 (Internal Server Error) if the orderItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-items")
    public ResponseEntity<OrderItem> updateOrderItem(@Valid @RequestBody OrderItem orderItem) throws URISyntaxException {
        log.debug("REST request to update OrderItem : {}", orderItem);
        if (orderItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderItem result = orderItemService.save(orderItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-items : get all the orderItems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of orderItems in body
     */
    @GetMapping("/order-items")
    public ResponseEntity<List<OrderItem>> getAllOrderItems(OrderItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrderItems by criteria: {}", criteria);
        Page<OrderItem> page = orderItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-items");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /order-items/count : count all the orderItems.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/order-items/count")
    public ResponseEntity<Long> countOrderItems(OrderItemCriteria criteria) {
        log.debug("REST request to count OrderItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderItemQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /order-items/:id : get the "id" orderItem.
     *
     * @param id the id of the orderItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderItem, or with status 404 (Not Found)
     */
    @GetMapping("/order-items/{id}")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable Long id) {
        log.debug("REST request to get OrderItem : {}", id);
        Optional<OrderItem> orderItem = orderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItem);
    }

    /**
     * DELETE  /order-items/:id : delete the "id" orderItem.
     *
     * @param id the id of the orderItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-items/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete OrderItem : {}", id);
        orderItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
