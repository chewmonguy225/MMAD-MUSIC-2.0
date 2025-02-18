package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;

//@Entity
public class Song extends Item{
    private int albumID;
    private int artistID;

    public Song(String sourceId, String name, int artistID, int albumID){
        super(sourceId, name);
        this.artistID = artistID;
        this.albumID = albumID;
    }
    public Song(int id,String sourceId, String name, int artistID, int albumID){
        super(id, sourceId, name);
        this.artistID = artistID;
        this.albumID = albumID;
    }

    public void setID(int id){
        this.id = id;
    }

    public int getAlbum(){
        return this.albumID;
    }

    public int getArtist(){
        return this.artistID;
    }
}