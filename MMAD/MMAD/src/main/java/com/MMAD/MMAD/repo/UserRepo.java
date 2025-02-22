package com.MMAD.MMAD.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MMAD.MMAD.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);
    void deleteUserById(Long id);
    void deleteByUsernameAndPassword(String username, String password);
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    
}
