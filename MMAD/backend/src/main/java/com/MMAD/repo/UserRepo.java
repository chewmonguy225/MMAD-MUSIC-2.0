package com.MMAD.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.MMAD.model.User.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    void deleteUserById(Long id);

    void deleteByUsernameAndPassword(String username, String password);

    Optional<User> findUserByUsernameAndPassword(String username, String password);

    List<User> findByUsernameContainingIgnoreCase(String username);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_friends WHERE user_id = :userId OR friend_id = :userId", nativeQuery = true)
    void deleteUserFromFriends(Long userId);
}
