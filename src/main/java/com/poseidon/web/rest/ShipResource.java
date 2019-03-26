package com.poseidon.web.rest;
import com.poseidon.domain.Ship;
import com.poseidon.service.ShipService;
import com.poseidon.web.rest.errors.BadRequestAlertException;
import com.poseidon.web.rest.util.HeaderUtil;
import com.poseidon.web.rest.util.PaginationUtil;
import com.poseidon.service.dto.ShipCriteria;
import com.poseidon.service.ShipQueryService;
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
 * REST controller for managing Ship.
 */
@RestController
@RequestMapping("/api")
public class ShipResource {

    private final Logger log = LoggerFactory.getLogger(ShipResource.class);

    private static final String ENTITY_NAME = "ship";

    private final ShipService shipService;

    private final ShipQueryService shipQueryService;

    public ShipResource(ShipService shipService, ShipQueryService shipQueryService) {
        this.shipService = shipService;
        this.shipQueryService = shipQueryService;
    }

    /**
     * POST  /ships : Create a new ship.
     *
     * @param ship the ship to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ship, or with status 400 (Bad Request) if the ship has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ships")
    public ResponseEntity<Ship> createShip(@Valid @RequestBody Ship ship) throws URISyntaxException {
        log.debug("REST request to save Ship : {}", ship);
        if (ship.getId() != null) {
            throw new BadRequestAlertException("A new ship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ship result = shipService.save(ship);
        return ResponseEntity.created(new URI("/api/ships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ships : Updates an existing ship.
     *
     * @param ship the ship to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ship,
     * or with status 400 (Bad Request) if the ship is not valid,
     * or with status 500 (Internal Server Error) if the ship couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ships")
    public ResponseEntity<Ship> updateShip(@Valid @RequestBody Ship ship) throws URISyntaxException {
        log.debug("REST request to update Ship : {}", ship);
        if (ship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ship result = shipService.save(ship);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ship.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ships : get all the ships.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of ships in body
     */
    @GetMapping("/ships")
    public ResponseEntity<List<Ship>> getAllShips(ShipCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ships by criteria: {}", criteria);
        Page<Ship> page = shipQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ships");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /ships/count : count all the ships.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/ships/count")
    public ResponseEntity<Long> countShips(ShipCriteria criteria) {
        log.debug("REST request to count Ships by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /ships/:id : get the "id" ship.
     *
     * @param id the id of the ship to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ship, or with status 404 (Not Found)
     */
    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable Long id) {
        log.debug("REST request to get Ship : {}", id);
        Optional<Ship> ship = shipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ship);
    }

    /**
     * DELETE  /ships/:id : delete the "id" ship.
     *
     * @param id the id of the ship to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ships/{id}")
    public ResponseEntity<Void> deleteShip(@PathVariable Long id) {
        log.debug("REST request to delete Ship : {}", id);
        shipService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
