package com.MMAD.MMAD.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    private List<Playlist> playlists = new ArrayList<Playlist>(); 

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    private List<Review> reviews = new ArrayList<Review>(); 


    /**
     * Empty constructor for hibernate.
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
    

    public String getPassword(){
        return password;
    }


    public void setPassword(String password){
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        else if (password.length() < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters.");
        }
        else if (password.matches(".*\\s+.*")) {
            throw new IllegalArgumentException("Password cannot contain spaces");
        }
        else {
            this.password = password;
        }
    }


    public Set<User> getFriendList(){
        return friendList;
    }


    public void setFriendList(Set<User> friendList){
        if (friendList == null) {
            throw new IllegalArgumentException("Friend list cannot be null");
        }
        else if (friendList.contains(this)) {
            throw new IllegalArgumentException("Friend list cannot contain self");
        }
        else if (friendList.contains(null)) {
            throw new IllegalArgumentException("Friend list cannot contain null");
        }
        else {
            this.friendList = friendList;
        }
    }


    public List<Playlist> getPlaylists(){
        return playlists;
    }


    public void setPlaylists(List<Playlist> playlists){
        if (playlists == null) {
            throw new IllegalArgumentException("Playlists cannot be null");
        }
        else if (playlists.contains(null)) {
            throw new IllegalArgumentException("Playlists cannot contain null");
        }
        else {
            this.playlists = playlists;
        }
    }


    public List<Review> getReviews(){
        return reviews;
    }


    public void setReviews(List<Review> reviews){
        if (reviews == null) {
            throw new IllegalArgumentException("Reviews cannot be null");
        }
        else if (reviews.contains(null)) {
            throw new IllegalArgumentException("Reviews cannot contain null");
        }
        else {
            this.reviews = reviews;
        }
    }

}
