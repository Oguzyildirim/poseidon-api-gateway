package com.poseidon.web.rest;
import com.poseidon.service.ShipService;
import com.poseidon.web.rest.errors.BadRequestAlertException;
import com.poseidon.web.rest.util.HeaderUtil;
import com.poseidon.web.rest.util.PaginationUtil;
import com.poseidon.service.dto.ShipDTO;
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
 * REST controller for managing Ship.
 */
@RestController
@RequestMapping("/api")
public class ShipResource {

    private final Logger log = LoggerFactory.getLogger(ShipResource.class);

    private static final String ENTITY_NAME = "ship";

    private final ShipService shipService;

    public ShipResource(ShipService shipService) {
        this.shipService = shipService;
    }

    /**
     * POST  /ships : Create a new ship.
     *
     * @param shipDTO the shipDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shipDTO, or with status 400 (Bad Request) if the ship has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ships")
    public ResponseEntity<ShipDTO> createShip(@Valid @RequestBody ShipDTO shipDTO) throws URISyntaxException {
        log.debug("REST request to save Ship : {}", shipDTO);
        if (shipDTO.getId() != null) {
            throw new BadRequestAlertException("A new ship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShipDTO result = shipService.save(shipDTO);
        return ResponseEntity.created(new URI("/api/ships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ships : Updates an existing ship.
     *
     * @param shipDTO the shipDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shipDTO,
     * or with status 400 (Bad Request) if the shipDTO is not valid,
     * or with status 500 (Internal Server Error) if the shipDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ships")
    public ResponseEntity<ShipDTO> updateShip(@Valid @RequestBody ShipDTO shipDTO) throws URISyntaxException {
        log.debug("REST request to update Ship : {}", shipDTO);
        if (shipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShipDTO result = shipService.save(shipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shipDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ships : get all the ships.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ships in body
     */
    @GetMapping("/ships")
    public ResponseEntity<List<ShipDTO>> getAllShips(Pageable pageable) {
        log.debug("REST request to get a page of Ships");
        Page<ShipDTO> page = shipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ships");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /ships/:id : get the "id" ship.
     *
     * @param id the id of the shipDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shipDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ships/{id}")
    public ResponseEntity<ShipDTO> getShip(@PathVariable Long id) {
        log.debug("REST request to get Ship : {}", id);
        Optional<ShipDTO> shipDTO = shipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipDTO);
    }

    /**
     * DELETE  /ships/:id : delete the "id" ship.
     *
     * @param id the id of the shipDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ships/{id}")
    public ResponseEntity<Void> deleteShip(@PathVariable Long id) {
        log.debug("REST request to delete Ship : {}", id);
        shipService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ships?query=:query : search for the ship corresponding
     * to the query.
     *
     * @param query the query of the ship search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ships")
    public ResponseEntity<List<ShipDTO>> searchShips(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Ships for query {}", query);
        Page<ShipDTO> page = shipService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ships");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
