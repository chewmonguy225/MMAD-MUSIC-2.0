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
    private Set<User> friendList = new HashSet<User>(); // Initialize to avoid nulls
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // Cascade all operations from User to Playlist
    private List<Playlist> playlists = new ArrayList<Playlist>(); 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // Cascade all operations from User to Review
    private List<Review> reviews = new ArrayList<Review>(); 

    public User() {}

    //public User(String username, String password,)


    

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
