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


    public User addUser(User user) {
        Optional<User> existingUser = findUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        } else {
            return userRepo.save(user);
        }
    }

}
