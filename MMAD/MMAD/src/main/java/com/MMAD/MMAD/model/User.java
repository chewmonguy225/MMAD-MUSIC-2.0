package com.MMAD.MMAD.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToMany
    @JoinTable(name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friendList = new HashSet<User>(); 

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    // private List<Playlist> playlists = new ArrayList<Playlist>(); 

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    // private List<Review> reviews = new ArrayList<Review>(); 


    /**
     * Empty constructor for JPA.
     */
    public User() {} 


    /**
     * Constructor taking all parameters. 
     * id, friendList, playlists & reviews are all initialized automatically.
     * 
     * @param username The user's username.
     * @param password The user's password.
     */
    public User(String username, String password) { 
        this.username = username;
        this.password = password;
    }


    /**
     * Adds a friend to the user's friend list.
     *
     * @param friend The user to be added as a friend.
     * @throws IllegalArgumentException if the friend is already a friend, or if the friend is the user itself, or if the friend is null.
     */
    public void addFriend(User friend) {
        if (friendList.contains(friend)) {
            throw new IllegalArgumentException("User is already a friend");
        }
        else if (friend == this) {
            throw new IllegalArgumentException("Cannot add self as a friend");
        }
        else if (friend == null) {
            throw new IllegalArgumentException("Friend cannot be null");
        }
        else {
            friendList.add(friend);
        }
    }


    /**
     * Removes a friend from the user's friend list.
     *
     * @param friend The user to be removed as a friend.
     * @throws IllegalArgumentException if the friend is not a friend, or if the friend is the user itself, or if the friend is null.
     */
    public void removeFriend(User friend) {
        if (!friendList.contains(friend)) {
            throw new IllegalArgumentException("User is not a friend");
        }
        else if (friend == this) {
            throw new IllegalArgumentException("Cannot remove self as a friend");
        }
        else if (friend == null) {
            throw new IllegalArgumentException("Friend cannot be null");
        }
        else {
            friendList.remove(friend);
        }
    }


    /**
     * 
     * Getters and setters for all attributes below.
     * Validation is done in the setters.
     * 
     */

     
    public String getUsername(){
        return username;
    }


    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        else if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username length must be between 3 and 20 characters.");
        }
        else if (!username.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("Username must contain only letters and numbers");
        }
        else if (username.matches(".*\\s+.*")) {
            throw new IllegalArgumentException("Username cannot contain spaces");
        }
        else if (username.matches(".*[^a-zA-Z0-9]+.*")) {
            throw new IllegalArgumentException("Username cannot contain special characters");
        }
        else {
            // ** NOTE: In order to check if the username is already taken, we need a repo method that queries the User table *
            // **       We need to do that here. once all validation done we set username below
            this.username = username;
        }
    }


    public Long getId(){
        return id;
    }
    

    public void setId(Long id){
        this.id = id;
    }
    

    public String getPassword() {
        return password;
    }


    public void setPassword(String password){
        this.password = password;
    }


    public Set<User> getFriendsList() {
        return friendList;
    }
} 
