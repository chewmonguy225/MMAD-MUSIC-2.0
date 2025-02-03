package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;

//@Entity
public class Song extends Item{
    private Album album;
    private Artist artist;

    public Song(String source_id, String name, Artist artist, Album album){
        super(source_id, name);
        this.artist = artist;
        this.album = album;
    }
    public Song(int id,String source_id, String name, Artist artist, Album album){
        super(id, source_id, name);
        this.artist = artist;
        this.album = album;
    }

    public void setID(int id){
        this.id = id;
    }

    public Album getAlbum(){
        return this.album;
    }

    public Artist getArtist(){
        return this.artist;
    }
}