package com.MMAD.MMAD.resource;

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

    public UserResource(UserService userService){
        this.userService = userService;
    }


    @GetMapping("/find/id")
    public ResponseEntity<User> getUserById(@RequestParam("id") Long id) {
        try {
            User user = userService.findUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/find/username")
    public ResponseEntity<User> getUserByUsername(@RequestParam("username") String username) {
        try {
            User user = userService.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            User user = userService.findUserByUsernameAndPassword(username, password).orElseThrow(() -> new RuntimeException("User not found"));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    

    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok().body(userService.createUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @Transactional
    @GetMapping("/delete")
    public ResponseEntity<String> deleteUserById(@RequestParam("id") Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
    


}
