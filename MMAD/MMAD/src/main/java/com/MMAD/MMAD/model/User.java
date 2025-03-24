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
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
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
                joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
    private Set<User> friendList = new HashSet<>(); 

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    private List<Playlist> playlists = new ArrayList<Playlist>(); 

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    // private List<Review> reviews = new ArrayList<Review>(); 


    /**
     * Empty constructor for JPA.
     */
    public User() {} 


    /**
     * Constructor
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
     * Constructor taking all parameters. 
     * id, friendList, playlists & reviews are all initialized automatically.
     * 
     * @param username The user's username.
     * @param password The user's password.
     */
    public User(Long id, String username, String password) { 
        this.id = id;
        this.username = username;
        this.password = password;
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

    public List<Playlist> getPlaylists() {
        return playlists;
    }
} 
