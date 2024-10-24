package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Authority;
import com.ecomm.jun.entity.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @BeforeEach
    void setUp() {
        saveAuthority(Role.ADMIN);
        saveAuthority(Role.USER);
    }

    private void saveAuthority(Role role) {
        Optional<Authority> existingAuthority = authorityRepository.findByAuthority(role);
        if (existingAuthority.isEmpty()) {
            Authority authority = new Authority();
            authority.setAuthority(role);
            authorityRepository.save(authority);
        }
    }

    @Test
    void findByAuthority() {
        // Act
        Optional<Authority> foundAuthority = authorityRepository.findByAuthority(Role.ADMIN);

        // Assert
        assertTrue(foundAuthority.isPresent(), "Expected authority should be present");
        assertEquals(Role.ADMIN.name(), foundAuthority.get().getAuthority(), "Authority should match");
    }


}