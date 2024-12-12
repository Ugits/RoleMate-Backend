package org.jonas.rolemate_backend.user.repository;

import org.jonas.rolemate_backend.user.model.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {

    Optional<CustomUser> findByUsername(String username);
    Optional<CustomUser> findByUsernameIgnoreCase(String username);

}
