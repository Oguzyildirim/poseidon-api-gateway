package com.poseidon.service;

import com.poseidon.domain.Authority;
import com.poseidon.domain.Company;
import com.poseidon.domain.Ship;
import com.poseidon.domain.User;
import com.poseidon.repository.AuthorityRepository;
import com.poseidon.repository.CompanyRepository;
import com.poseidon.repository.ShipRepository;
import com.poseidon.repository.UserRepository;
import com.poseidon.repository.search.ShipSearchRepository;
import com.poseidon.security.AuthoritiesConstants;
import com.poseidon.service.dto.ShipDTO;
import com.poseidon.service.mapper.ShipMapper;
import com.poseidon.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ship.
 */
@Service
@Transactional
public class ShipService {

    private final Logger log = LoggerFactory.getLogger(ShipService.class);

    private final ShipRepository shipRepository;

    private final ShipMapper shipMapper;

    private final ShipSearchRepository shipSearchRepository;

    private final UserService userService;

    private final AuthorityRepository authorityRepository;

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    public ShipService(ShipRepository shipRepository, ShipMapper shipMapper, ShipSearchRepository shipSearchRepository, UserService userService, AuthorityRepository authorityRepository, CompanyRepository companyRepository, UserRepository userRepository) {
        this.shipRepository = shipRepository;
        this.shipMapper = shipMapper;
        this.shipSearchRepository = shipSearchRepository;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Register a Ship user.
     *
     * @param managedUserVM the managed user View Model
     * @param shipDTO The ShipDTO
     * @param login The user login
     */
    public Ship registerShip(ManagedUserVM managedUserVM, ShipDTO shipDTO, String login){
        User newUser = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.SHIP).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        Ship newShip = new Ship();
        newShip.setUser(newUser);

        Optional<User> companyUser = userRepository.findOneByLogin(login);
        Optional<Company> shipCompany = (Optional<Company>) companyRepository.findOneByUser(companyUser);

        //newShip.setCompany(shipCompany);
        newShip.setShipId(shipDTO.getShipId());
        newShip.setPhone(shipDTO.getPhone());
        log.debug("Created Information for Ship: {}", newShip);
        return newShip;
    }

    /**
     * Save a ship.
     *
     * @param shipDTO the entity to save
     * @return the persisted entity
     */
    public ShipDTO save(ShipDTO shipDTO) {
        log.debug("Request to save Ship : {}", shipDTO);
        Ship ship = shipMapper.toEntity(shipDTO);
        ship = shipRepository.save(ship);
        ShipDTO result = shipMapper.toDto(ship);
        shipSearchRepository.save(ship);
        return result;
    }

    /**
     * Get all the ships.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ships");
        return shipRepository.findAll(pageable)
            .map(shipMapper::toDto);
    }


    /**
     * Get one ship by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ShipDTO> findOne(Long id) {
        log.debug("Request to get Ship : {}", id);
        return shipRepository.findById(id)
            .map(shipMapper::toDto);
    }

    /**
     * Delete the ship by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ship : {}", id);
        shipRepository.deleteById(id);
        shipSearchRepository.deleteById(id);
    }

    /**
     * Search for the ship corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShipDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ships for query {}", query);
        return shipSearchRepository.search(queryStringQuery(query), pageable)
            .map(shipMapper::toDto);
    }
}
