package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;

@Entity
public class Artist extends Item {

    public Artist() {
        super();
    }

    public Artist(String sourceID, String name) {
        super(sourceID, name);
    }

    public Artist(String imageURL, String sourceID, String name) {
        super(imageURL, sourceID, name);
    }

    public Artist(int id, String sourceID, String name) {
        super(id, sourceID, name);
    }

    public void setID(int id) {
        this.id = id;
    }
}
