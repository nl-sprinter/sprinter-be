package com.nl.sprinterbe.user.repository;

import com.nl.sprinterbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Override
    Optional<User> findById(Long id);

    User findByEmailAndProvider(String email,String provider);
}
