package com.poseidon.service;

import com.poseidon.domain.Authority;
import com.poseidon.domain.Company;
import com.poseidon.domain.User;
import com.poseidon.repository.AuthorityRepository;
import com.poseidon.repository.CompanyRepository;
import com.poseidon.security.AuthoritiesConstants;
import com.poseidon.service.dto.CompanyDTO;
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

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final UserService userService;

    private final AuthorityRepository authorityRepository;

    public CompanyService(CompanyRepository companyRepository, UserService userService, AuthorityRepository authorityRepository) {
        this.companyRepository = companyRepository;
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
     * @param company the entity to save
     * @return the persisted entity
     */
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable);
    }


    /**
     * Get one company by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
