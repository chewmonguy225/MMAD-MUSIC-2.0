package com.MMAD.MMAD.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MMAD.MMAD.model.User;

import jakarta.transaction.Transactional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);
    void deleteUserById(Long id);
    void deleteByUsernameAndPassword(String username, String password);
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.friendList = :friendList WHERE u.id = :userId")
    void updateFriendList(@Param("userId") Long userId, @Param("friendList") Set<User> friendList);
}
