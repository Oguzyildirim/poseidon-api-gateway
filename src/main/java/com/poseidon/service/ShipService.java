package com.poseidon.service;

import com.poseidon.domain.Ship;
import com.poseidon.repository.ShipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Ship.
 */
@Service
@Transactional
public class ShipService {

    private final Logger log = LoggerFactory.getLogger(ShipService.class);

    private final ShipRepository shipRepository;

    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    /**
     * Save a ship.
     *
     * @param ship the entity to save
     * @return the persisted entity
     */
    public Ship save(Ship ship) {
        log.debug("Request to save Ship : {}", ship);
        return shipRepository.save(ship);
    }

    /**
     * Get all the ships.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ship> findAll(Pageable pageable) {
        log.debug("Request to get all Ships");
        return shipRepository.findAll(pageable);
    }


    /**
     * Get one ship by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Ship> findOne(Long id) {
        log.debug("Request to get Ship : {}", id);
        return shipRepository.findById(id);
    }

    /**
     * Delete the ship by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ship : {}", id);
        shipRepository.deleteById(id);
    }
}
