package com.poseidon.service;

import com.poseidon.domain.Authority;
import com.poseidon.domain.Company;
import com.poseidon.domain.User;
import com.poseidon.repository.AuthorityRepository;
import com.poseidon.repository.CompanyRepository;
import com.poseidon.repository.search.CompanySearchRepository;
import com.poseidon.security.AuthoritiesConstants;
import com.poseidon.service.dto.CompanyDTO;
import com.poseidon.service.mapper.CompanyMapper;
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
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final CompanySearchRepository companySearchRepository;

    private final UserService userService;

    private final AuthorityRepository authorityRepository;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper, CompanySearchRepository companySearchRepository, UserService userService, AuthorityRepository authorityRepository) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.companySearchRepository = companySearchRepository;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Register a Company user
     *
     * @param companyDTO the companyDTO
     * @param managedUserVM the ManagedUserVM for View Model
     * @return newCompany
     */
    public Company registerCompany(CompanyDTO companyDTO, ManagedUserVM managedUserVM){
        User newUser = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.COMPANY).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        Company newCompany = new Company();
        newCompany.setUser(newUser);
        newCompany.setName(companyDTO.getName());
        newCompany.setPhone(companyDTO.getPhone());
        log.debug("Created Information for Company: {}", newCompany);
        return newCompany;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save
     * @return the persisted entity
     */
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        CompanyDTO result = companyMapper.toDto(company);
        companySearchRepository.save(company);
        return result;
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable)
            .map(companyMapper::toDto);
    }


    /**
     * Get one company by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id)
            .map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
        companySearchRepository.deleteById(id);
    }

    /**
     * Search for the company corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CompanyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Companies for query {}", query);
        return companySearchRepository.search(queryStringQuery(query), pageable)
            .map(companyMapper::toDto);
    }
}
