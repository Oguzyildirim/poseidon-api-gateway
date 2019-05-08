package com.poseidon.repository;

import com.poseidon.domain.Company;
import com.poseidon.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findOneByUser(Optional<User> user);
}
