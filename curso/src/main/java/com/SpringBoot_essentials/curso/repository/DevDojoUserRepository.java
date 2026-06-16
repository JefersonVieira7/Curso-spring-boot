package com.SpringBoot_essentials.curso.repository;

import com.SpringBoot_essentials.curso.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {
    Optional<DevDojoUser> findByUsername(String username);
}
