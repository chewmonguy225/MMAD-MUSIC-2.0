package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;

@Entity
public class Album extends Item {

    private Artist artist;


    public Album(String sourceID, String name, Artist artist){
        super(sourceID, name);
        this.artist = artist;
    }


    public Artist artist(){
        return this.artist;
    }


    public Artist getArtist(){
        return this.artist;
    }

}
