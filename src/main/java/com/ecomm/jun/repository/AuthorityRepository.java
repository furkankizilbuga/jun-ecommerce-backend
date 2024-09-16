package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Authority;
import com.ecomm.jun.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("SELECT a FROM Authority a WHERE a.authority = :authority")
    Optional<Authority> findByAuthority(Role authority);
}
