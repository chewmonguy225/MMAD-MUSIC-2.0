package com.MMAD.MMAD.resource;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.model.User.UserDTO;
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
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get a user by their id.
     * 
     * @param id The id of the user to be retrieved.
     * @return The user with the given id.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        try {
            UserDTO user = userService.findUserById(id);
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
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable("username") String username) {
        try {
            UserDTO user = userService.findUserByUsername(username);
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
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            UserDTO user = userService.findUserByUsernameAndPassword(username, password);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Create a new user.
     * 
     * @param user The user to be created.
     * @return The created user DTO if successful.
     */
    @PostMapping("/create")
    public ResponseEntity<UserDTO> addUser(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            return ResponseEntity.ok().body(userService.createUser(username, password));
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
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Follow a user (add to following list).
     * 
     * @param username       The username of the user who wants to follow another
     *                       user.
     * @param followUsername The username of the user to be followed.
     * @return A message indicating success.
     */
    @Transactional
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestParam("username") String username,
            @RequestParam("followUsername") String followUsername) {
        try {
            userService.findUserByUsername(username);
            userService.findUserByUsername(followUsername);
            userService.followUser(username, followUsername); // this method must be updated to accept usernames
            return ResponseEntity.ok("User followed successfully");
        } catch (Exception e) {
            System.err.println("Error following user: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestParam("username") String username,
            @RequestParam("unfollowUsername") String unfollowUsername) {
        try {
            userService.findUserByUsername(username);
            userService.findUserByUsername(unfollowUsername);
            userService.unfollowUser(username, unfollowUsername); // implement this in your service
            return ResponseEntity.ok("User unfollowed successfully");
        } catch (Exception e) {
            System.err.println("Error unfollowing user: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get list of users that the user is following.
     * 
     * @param userId The id of the user.
     * @return List of UserDTO representing the users being followed.
     */
    @GetMapping("/following/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowing(@PathVariable("userId") Long userId) {
        try {
            List<UserDTO> following = userService.getFollowing(userId);
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            System.err.println("Error retrieving following list: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Get list of followers of a user.
     * 
     * @param userId The id of the user.
     * @return List of UserDTO representing the followers.
     */
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable("userId") Long userId) {
        try {
            List<UserDTO> followers = userService.getFollowers(userId);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            System.err.println("Error retrieving followers list: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Remove all followings of a user (stop following everyone).
     * 
     * @param userId The id of the user.
     * @return Success message.
     */
    @PostMapping("/following/removeAll/{userId}")
    public ResponseEntity<String> removeAllFollowing(@PathVariable("userId") Long userId) {
        try {
            userService.removeAllFollowing(userId);
            return ResponseEntity.ok("All followings removed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Remove all followers of a user (remove all followers).
     * 
     * @param userId The id of the user.
     * @return Success message.
     */
    @PostMapping("/followers/removeAll/{userId}")
    public ResponseEntity<String> removeAllFollowers(@PathVariable("userId") Long userId) {
        try {
            userService.removeAllFollowers(userId);
            return ResponseEntity.ok("All followers removed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- NEW ENDPOINT FOR SEARCHING USERS ---
    /**
     * Searches for users whose usernames contain the given query string
     * (case-insensitive).
     *
     * @param query The string to search for within usernames.
     * @return A list of UserDTOs matching the search criteria.
     */

    // --- NEW ENDPOINT FOR SEARCHING USERS (Similar to removeAllFriends) ---
    /**
     * Searches for users whose usernames contain the given query string
     * (case-insensitive).
     *
     * @param query The string to search for within usernames.
     * @return A list of UserDTOs matching the search criteria.
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam("query") String query) {
        try {
            List<UserDTO> users = userService.searchUsers(query);
            // If no users are found, userService.searchUsers returns an empty list,
            // which is a valid success response (HTTP 200 OK with an empty array).
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            // Catch any unexpected exceptions that might occur during the service call
            // and return a bad request with the error message, similar to your template.
            return ResponseEntity.badRequest().body(null); // Returning null body with badRequest indicates an error.
                                                           // You might prefer to return e.getMessage() here
                                                           // or a more specific error response.
        }
    }

}
