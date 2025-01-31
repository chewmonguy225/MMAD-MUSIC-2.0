package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;

@Entity
public class Song extends Item{

    private Album album;
    private Artist artist;


    /**
     * Constructor taking all parameters.
     * 
     * @param sourceID The song's source ID (spotify).
     * @param name The song's name.
     * @param artist The song's artist.
     * @param album The song's album.
     */
    public Song(String sourceID, String name, Artist artist, Album album){
        super(sourceID, name);
        this.artist = artist;
        this.album = album;
    }

    
    /**
     * Constructor taking all parameters except for album. Used for singles.
     *
     * @param sourceID The song's source ID (spotify).
     * @param name The song's name.
     * @param artist The song's artist.
     */
    public Song(String sourceID, String name, Artist artist){
        super(sourceID, name);
        this.artist = artist;
    }

    public Album getAlbum(){
        return this.album;
    }


    public Artist getArtist(){
        return this.artist;
    }
}