package com.MMAD.MMAD.Service;

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


    public User addUser(User user) {
        Optional<User> existingUser = findUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        } else {
            return userRepo.save(user);
        }
    }


    public User updateUser(User user) {
        Optional<User> existingUser = findUserById(user.getId());
        if (existingUser.isPresent()) {
            return userRepo.save(user);
        } else {
            throw new RuntimeException("User does not exist");
        }
    }


    public void deleteUserByUsernameAndPassword(String username, String password) {
        Optional<User> existingUser = findUserByUsernameAndPassword(username, password);
        if (existingUser.isPresent()) {
            userRepo.deleteByUsernameAndPassword(username, password);
        } else {
            throw new RuntimeException("User does not exist");
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


}
