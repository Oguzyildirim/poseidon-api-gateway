package com.poseidon.repository;

import com.poseidon.domain.Ship;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

}
