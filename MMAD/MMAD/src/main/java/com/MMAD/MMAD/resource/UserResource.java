package com.MMAD.MMAD.resource;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.model.User;
import com.MMAD.MMAD.service.UserService;

import jakarta.transaction.Transactional;



@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;


    /**
     * Constructor for UserResource.
     * 
     * @param userService The UserService to be used.
     */
    public UserResource(UserService userService){
        this.userService = userService;
    }


    /**
     * Get a user by their id.
     * 
     * @param id The id of the user to be retrieved.
     * @return The user with the given id.
     */
    @GetMapping("/find/id")
    public ResponseEntity<User> getUserById(@RequestParam("id") Long id) {
        try {
            User user = userService.findUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Get a user by their username.
     * 
     * @param username The username of the user to be retrieved.
     * @return The user with the given username.
     */
    @GetMapping("/find/username")
    public ResponseEntity<User> getUserByUsername(@RequestParam("username") String username) {
        try {
            User user = userService.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Get a user by their username and password.
     * 
     * @param username The username of the user to be retrieved.
     * @param password The password of the user to be retrieved.
     * @return The user with the given username and password.
     */
    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            User user = userService.findUserByUsernameAndPassword(username, password).orElseThrow(() -> new RuntimeException("User not found"));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    

    /**
     * Create a new user.
     * 
     * @param user The user to be created.
     * @return The created user.
     */
    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok().body(userService.createUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Delete a user by their id.
     * 
     * @param id The id of the user to be deleted.
     * @return A message indicating the success of the deletion.
     */
    @Transactional
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUserById(@RequestParam("id") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /**
     * Add a friend to a user's friend list.
     * 
     * @param id The id of the user to add a friend to.
     * @param friendId The id of the friend to be added.
     * @return A message indicating the success of the addition.
     */
    @Transactional
    @PostMapping("/friends/add")
    public ResponseEntity<String> addFriend(@RequestParam("id") Long id, @RequestParam("friendId") Long friendId) {
        try {
            User user = userService.findUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
            User friend = userService.findUserById(friendId).orElseThrow(() -> new RuntimeException("Friend not found"));
            userService.addFriend(id, friendId);
            return ResponseEntity.ok().body("User added successfully");
        } catch (Exception e) {
            System.err.println("Error adding friend: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /**
     * Get the friends list of a user.
     * 
     * @param id The id of the user whose friends list is being retrieved.
     * @return A Set of User objects which are in friends list of the user with the provided id.
     */
    @GetMapping("/friends")
    public ResponseEntity<Set<User>> getFriendList(@RequestParam("id") Long id) {
        try {
            Set<User> friends = userService.getFriendList(id);
            return new ResponseEntity<>(friends, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error retrieving friends list: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Remove all friends from a user's friend list.
     * 
     * @param id The id of the user to remove a friend from.
     * @return A message indicating the success of the removal.
     */
    @PostMapping("/friends/removeAll")
    public ResponseEntity<String> removeAllFriends(@RequestParam("id") Long id) {
        try {
            userService.removeAllFriends(id);
            return ResponseEntity.ok().body("All friends removed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    


}
