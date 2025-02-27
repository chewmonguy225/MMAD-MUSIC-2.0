package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;

//@Entity
public class Song extends Item{
    private Long albumID;
    private Long artistID;

    public Song(String sourceId, String name, Long artistID, Long albumID){
        super(sourceId, name);
        this.artistID = artistID;
        this.albumID = albumID;
    }
    public Song(Long id,String sourceId, String name, Long artistID, Long albumID){
        super(id, sourceId, name);
        this.artistID = artistID;
        this.albumID = albumID;
    }

    public void setID(Long id){
        this.id = id;
    }

    public Long getAlbum(){
        return this.albumID;
    }

    public Long getArtist(){
        return this.artistID;
    }
}