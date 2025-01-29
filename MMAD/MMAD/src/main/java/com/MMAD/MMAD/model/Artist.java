package com.MMAD.MMAD.model;

@Entity
public class Artist extends Item {

    public Artist(String sourceID, String name){
        super(sourceID, name);
    }
    public Artist(int id, String sourceID, String name){
        super(id, sourceID, name);
    }

    public void setID(int id){
        this.id = id;
    }
}
