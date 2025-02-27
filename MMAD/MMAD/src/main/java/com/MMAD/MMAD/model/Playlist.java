package com.MMAD.MMAD.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private Long id;

    private User user;

    private String name;

    // @ManyToMany
    // @JoinTable(
    //     name = "playlist_song",
    //     joinColumns = @JoinColumn(name = "playlist_id"),
    //     inverseJoinColumns = @JoinColumn(name = "song_id")
    // )
    // private Set<Long> songIDs = new HashSet<>();

   
    /**
     * Getters and setter's
     */

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    // public Set<Long> getSongIDs() {
    //     return songIDs;
    // }


    // public void setSongs(Set<Long> songIDs) {
    //     this.songIDs = songIDs;
    // }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

}
