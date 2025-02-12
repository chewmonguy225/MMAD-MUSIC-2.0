package com.MMAD.MMAD.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.MMAD.MMAD.model.User;
import com.MMAD.MMAD.repo.UserRepo;

@Service
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


    // public Optional<User> addFriend(User user, User friend) {
    //     Optional<User> existingUser = findUserById(user.getId());
    //     Optional<User> existingFriend = findUserById(friend.getId());
    //     if (existingUser.isPresent() && existingFriend.isPresent()) {
    //         userRepo.addFriend(user, friend);
    //         return findUserById(user.getId());
    //     } else {
    //         throw new RuntimeException("User does not exist");
    //     }
    // }


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
