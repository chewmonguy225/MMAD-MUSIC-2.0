package com.MMAD.MMAD.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.MMAD.MMAD.model.User;


public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);
    void deleteUserById(Long id);
    void deleteByUsernameAndPassword(String username, String password);
    Optional<User> findUserByUsernameAndPassword(String username, String password);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_friends WHERE user_id = :userId OR friend_id = :userId", nativeQuery = true)
    void deleteUserFromFriends(Long userId);
}
