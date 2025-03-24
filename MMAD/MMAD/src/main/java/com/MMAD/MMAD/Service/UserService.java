package com.MMAD.MMAD.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.MMAD.MMAD.exception.UserNotFoundException;
import com.MMAD.MMAD.model.User;
import com.MMAD.MMAD.model.UserDTO;
import com.MMAD.MMAD.model.UserDTOMapper;
import com.MMAD.MMAD.repo.UserRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;
    
    private final UserDTOMapper userDTOMapper;


    /**
     * Constructor for UserService.
     * 
     * @param userRepo The UserRepo to be used.
     * @throws RuntimeException if the userRepo or userDTOMapper are null
     */
    public UserService(UserRepo userRepo, UserDTOMapper userDTOMapper) {
        if(userRepo == null){
            throw new RuntimeException("userRepo cannot be null");
        } else if(userDTOMapper == null){
            throw new RuntimeException("userDTOMapper cannot be null");
        } else {
            this.userRepo = userRepo;
            this.userDTOMapper = userDTOMapper;
        }
    }


    /**
     * Get a user by their id.
     * 
     * @param id The id of the user to be retrieved.
     * @return UserDTO object for the found user if succussful
     * @throws UserNotFoundException if the user does not exist.
     */
    public UserDTO findUserById(Long id) {
        return userRepo.findUserById(id)
                .map(userDTOMapper::apply)
                .orElseThrow(() -> new UserNotFoundException("user with id [%s] not found".formatted(id)));
    }


    /**
     * Get a user by their username.
     * 
     * @param username The username of the user to be retrieved.
     * @return UserDTO object for the found user if succussful
     * @throws UserNotFoundException if the user does not exist.
     */
    public UserDTO findUserByUsername(String username) {
        return userRepo.findUserByUsername(username)
                .map(userDTOMapper::apply) 
                .orElseThrow(() -> new UserNotFoundException("user with username [%s] not found".formatted(username)));
    }


    /**
     * Get a user by their username and password.
     * 
     * @param username The username of the user to be retrieved.
     * @param password The password of the user to be retrieved.
     * @return UserDTO object for the found user if succussful
     * @throws UserNotFoundException if the user does not exist.
     */
    public UserDTO findUserByUsernameAndPassword(String username, String password) {
        return userRepo.findUserByUsernameAndPassword(username, password)
                .map(userDTOMapper::apply) 
                .orElseThrow(() -> new UserNotFoundException("credentials do not match"));
    }


    /**
     * Create a new user.
     * 
     * @param user The user to be created.
     * @return The created User object.
     * @throws RuntimeException if the user already exists.
     */
    public UserDTO createUser(String username, String password) {
        Optional<User> existingUser = userRepo.findUserByUsername(username);
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        } else {
            User newUser = new User(username, password);
            userRepo.save(newUser);
            return userDTOMapper.apply(newUser);
        }
    }


    /**
     * Delete a user by id.
     * 
     * @param id The id of the user to be deleted.
     * @throws RuntimeException if the user does not exist.
     */
    public void deleteUser(Long id) {
        Optional<User> existingUser = userRepo.findUserById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            userRepo.deleteUserFromFriends(id); // Remove the user from all friend lists
            user.getFriendsList().clear(); // Clear the user's friend list
            userRepo.delete(user);
        } else {
            throw new RuntimeException("User does not exist");
        }
    }


    /**
     * Adds a friend to the user's friend list.
     * 
     * @param userId The id of the user to add a friend to.
     * @param friendId The id of the friend to be added.
     * @throws IllegalArgumentException if the friend is already a friend, or if the friend is the user itself, or if the friend is null.
     */
    public void addFriend(Long userId, Long friendId) {

        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
            Set<User> friendList = user.getFriendsList();
            User friend = userRepo.findById(friendId).orElseThrow(() -> new EntityNotFoundException("Friend not found"));
            if (friendList.contains(friend)) {
                throw new IllegalArgumentException("User is already a friend");
            } 
            else if (friend == user) {
                throw new IllegalArgumentException("Cannot add self as a friend");
            } 
            else if (friend == null) {
                throw new IllegalArgumentException("Friend cannot be null");
            }
            else {
                friendList.add(friend);
                userRepo.save(user);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding friend: " + e.getMessage());
            throw new RuntimeException("Error adding friend");}
    }


    /**
     * Get the friend list of a user.
     * 
     * @param userId The id of the user whose friend list is to be retrieved.
     * @return A set of User objects representing the friends of the user.
     * @throws RuntimeException if the user does not exist.
     */
    public List<UserDTO> getFriendList(Long userId) {
        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            return user.getFriendsList()
                    .stream()
                    .map(userDTOMapper::apply)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting friend list: " + e.getMessage());
            throw new RuntimeException("Error getting friend list");
        }
    }


    /**
     * Removes a friend from the user's friend list.
     * 
     * @param userId The id of the user to remove a friend from.
     * @param friendId The id of the friend to be removed.
     * @throws IllegalArgumentException if the friend is not a friend, or if the friend is the user itself, or if the friend is null.
     */
    public void removeFriend(Long userId, Long friendId) {
        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
            Set<User> friendList = user.getFriendsList();
            User friend = userRepo.findById(friendId).orElseThrow(() -> new EntityNotFoundException("Friend not found"));
            if (!friendList.contains(friend)) {
                throw new IllegalArgumentException("User is not a friend");
            } 
            else if (friend == user) {
                throw new IllegalArgumentException("Cannot remove self as a friend");
            } 
            else if (friend == null) {
                throw new IllegalArgumentException("Friend cannot be null");
            }
            else {
                friendList.remove(friend);
                userRepo.save(user);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing friend: " + e.getMessage());
            throw new RuntimeException("Error removing friend");
        }
    }


    /**
     * Remove all friends from a user's friend list.
     * 
     * @param userId The id of the user whose friends are to be removed.
     * @throws RuntimeException if the user does not exist.
     */
    public void removeAllFriends(Long userId) {
        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Set<User> friends = user.getFriendsList();
            for (User friend : friends) {
                friend.getFriendsList().remove(user);
                userRepo.save(friend);
            }
            user.getFriendsList().clear();
            userRepo.save(user);
        } catch (Exception e) {
            System.err.println("Error removing all friends: " + e.getMessage());
            throw new RuntimeException("Error removing all friends");
        }
    }
}
