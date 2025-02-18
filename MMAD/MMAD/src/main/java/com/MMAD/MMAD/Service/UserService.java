package com.MMAD.MMAD.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.MMAD.MMAD.model.User;
import com.MMAD.MMAD.repo.UserRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;


    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public Optional<User> findUserById(Long id) {
        return userRepo.findUserById(id);
    }


    public Optional<User> findUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }


    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        return userRepo.findUserByUsernameAndPassword(username, password);
    }


    public User createUser(User user) {
        Optional<User> existingUser = findUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        } else {
            return userRepo.save(user);
        }
    }


    public void deleteUserById(Long id) {
        Optional<User> existingUser = findUserById(id);
        if (existingUser.isPresent()) {
            userRepo.deleteUserById(id);
        } else {
            throw new RuntimeException("User does not exist");
        }
    }


    public void addFriend(Long userId, Long friendId) {

        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
            Set<User> friendList = user.getFriendsList();
            User friend = userRepo.findById(friendId).orElseThrow(() -> new EntityNotFoundException("Friend not found"));
            friendList.add(friend);
            updateFriendList(userId, friendList);
        } catch (Exception e) {
            System.err.println("Error adding friend: " + e.getMessage());
            throw new RuntimeException("Error adding friend");}
    }


    @Transactional
    void updateFriendList(Long userId, Set<User> newFriendList) {
        Optional<User> userOptional = userRepo.findById(userId); 
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getFriendsList().clear(); // Remove existing friends
            user.getFriendsList().addAll(newFriendList); // Add new friends
            userRepo.save(user); // Save the changes
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }


    // public void removeFriend(User user, User friend) {
    //     Optional<User> existingUser = findUserById(user.getId());
    //     Optional<User> existingFriend = findUserById(friend.getId());
    //     if (existingUser.isPresent() && existingFriend.isPresent()) {
    //         userRepo.deleteUserById(friend.getId());
    //     } else {
    //         throw new RuntimeException("User does not exist");
    //     }
    // }


}
