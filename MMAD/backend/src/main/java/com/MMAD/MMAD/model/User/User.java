package com.MMAD.MMAD.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.MMAD.MMAD.model.User.Playlist;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // -----------------------------
    // FOLLOWERS / FOLLOWING Logic
    // -----------------------------

    // Users this user is following
    @ManyToMany
    @JoinTable(
        name = "user_following",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();

    // Users who follow this user
    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    // -----------------------------
    // Other Relationships
    // -----------------------------

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Playlist> playlists = new ArrayList<>();

    // -----------------------------
    // Constructors
    // -----------------------------

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // -----------------------------
    // Getters & Setters
    // -----------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        } else if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username length must be between 3 and 20 characters.");
        } else if (!username.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("Username must contain only letters and numbers");
        } else {
            this.username = username;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Add validation logic if needed
        this.password = password;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    // -----------------------------
    // Helper Methods
    // -----------------------------

    public void follow(User target) {
        this.following.add(target);
        target.followers.add(this);
    }

    public void unfollow(User target) {
        this.following.remove(target);
        target.followers.remove(this);
    }
}
