package com.MMAD.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.MMAD.Security.JWTService;
import com.MMAD.dto.user.LoginResponse;
import com.MMAD.dto.user.UserDTO;
import com.MMAD.dto.user.UserDTOMapper;
import com.MMAD.dto.user.UserItemDTO;
import com.MMAD.exception.UserNotFoundException;
import com.MMAD.model.User.User;
import com.MMAD.repo.UserRepo;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public UserService(UserRepo userRepo, UserDTOMapper userDTOMapper,
            JWTService jwtService, PasswordEncoder passwordEncoder) {
        if (userRepo == null) {
            throw new RuntimeException("userRepo cannot be null");
        } else if (userDTOMapper == null) {
            throw new RuntimeException("userDTOMapper cannot be null");
        } else if (passwordEncoder == null) {
            throw new RuntimeException("userDTOMapper cannot be null");
        } else {
            this.userRepo = userRepo;
            this.userDTOMapper = userDTOMapper;
            this.passwordEncoder = passwordEncoder;
            this.jwtService = jwtService;
        }
    }

    @GetMapping("/me")
    public UserDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserDTOByUsername(username);
    }

    @Transactional
    public UserDTO getUserDTOById(Long id) {
        return userRepo.findUserById(id)
                .map(userDTOMapper::apply)
                .orElseThrow(() -> new UserNotFoundException("user with id [%s] not found".formatted(id)));
    }

    @Transactional
    public UserDTO getUserDTOByUsername(String username) {
        return userRepo.findUserByUsername(username)
                .map(userDTOMapper::apply)
                .orElseThrow(() -> new UserNotFoundException("user with username [%s] not found".formatted(username)));
    }

    @Transactional
    public Optional<User> getUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }

    public LoginResponse login(String username, String password) {

        User user = userRepo.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(token, user.getUsername());
    }

    public UserDTO createUser(String username, String password) {
        Optional<User> existingUser = userRepo.findUserByUsername(username);
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        } else {
            User newUser = new User(
                    username,
                    passwordEncoder.encode(password));
            userRepo.save(newUser);
            return userDTOMapper.apply(newUser);
        }
    }

    public void deleteUser(Long id) {
        Optional<User> existingUser = userRepo.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Remove this user from other users' followers and following lists
            user.getFollowers().forEach(follower -> {
                follower.getFollowing().remove(user);
                userRepo.save(follower);
            });

            user.getFollowing().forEach(followingUser -> {
                followingUser.getFollowers().remove(user);
                userRepo.save(followingUser);
            });

            user.getFollowers().clear();
            user.getFollowing().clear();

            userRepo.delete(user);
        } else {
            throw new RuntimeException("User does not exist");
        }
    }

    /**
     * User 'username' follows the user 'toFollowUsername'
     */
    public void followUser(String username, String toFollowUsername) {
        try {
            if (username.equals(toFollowUsername)) {
                throw new IllegalArgumentException("Cannot follow yourself");
            }

            User user = userRepo.findUserByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            User toFollow = userRepo.findUserByUsername(toFollowUsername)
                    .orElseThrow(() -> new EntityNotFoundException("User to follow not found"));

            if (user.getFollowing().contains(toFollow)) {
                throw new IllegalArgumentException("Already following this user");
            }

            user.getFollowing().add(toFollow);
            toFollow.getFollowers().add(user);

            userRepo.save(user);
            userRepo.save(toFollow);

        } catch (IllegalArgumentException e) {
            System.err.println("Error following user: " + e.getMessage());
            throw new RuntimeException("Error following user: " + e.getMessage());
        }
    }

    public void unfollowUser(String username, String toUnfollowUsername) {
        try {
            if (username.equals(toUnfollowUsername)) {
                throw new IllegalArgumentException("Cannot unfollow yourself");
            }

            User user = userRepo.findUserByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            User toUnfollow = userRepo.findUserByUsername(toUnfollowUsername)
                    .orElseThrow(() -> new EntityNotFoundException("User to unfollow not found"));

            if (!user.getFollowing().contains(toUnfollow)) {
                throw new IllegalArgumentException("Not following this user");
            }

            user.getFollowing().remove(toUnfollow);
            toUnfollow.getFollowers().remove(user);

            userRepo.save(user);
            userRepo.save(toUnfollow);

        } catch (IllegalArgumentException e) {
            System.err.println("Error unfollowing user: " + e.getMessage());
            throw new RuntimeException("Error unfollowing user: " + e.getMessage());
        }
    }

    /**
     * Get list of users the user is following.
     */
    public List<UserDTO> getFollowing(Long userId) {
        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            return user.getFollowing()
                    .stream()
                    .map(userDTOMapper::apply)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting following list: " + e.getMessage());
            throw new RuntimeException("Error getting following list");
        }
    }

    /**
     * Get list of followers of the user.
     */
    public List<UserDTO> getFollowers(Long userId) {
        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            return user.getFollowers()
                    .stream()
                    .map(userDTOMapper::apply)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting followers list: " + e.getMessage());
            throw new RuntimeException("Error getting followers list");
        }
    }

    /**
     * Searches for users whose usernames contain the given query string
     * (case-insensitive).
     */
    public List<UserItemDTO> searchUsers(String query) {
        List<User> users = userRepo.findByUsernameContainingIgnoreCase(query);

        return users.stream()
                .map(user -> new UserItemDTO(
                        user.getId(),
                        user.getUsername(),
                        "https://ui-avatars.com/api/?name=" + user.getUsername()))
                .collect(Collectors.toList());

    }

    /**
     * Remove all users that the given user is following.
     */
    public void removeAllFollowing(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Remove this user from the followers list of every followed user
        for (User followedUser : user.getFollowing()) {
            followedUser.getFollowers().remove(user);
            userRepo.save(followedUser);
        }

        user.getFollowing().clear();
        userRepo.save(user);
    }

    /**
     * Remove all followers of the given user.
     */
    public void removeAllFollowers(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Remove this user from the following list of all its followers
        for (User follower : user.getFollowers()) {
            follower.getFollowing().remove(user);
            userRepo.save(follower);
        }

        user.getFollowers().clear();
        userRepo.save(user);
    }
}
