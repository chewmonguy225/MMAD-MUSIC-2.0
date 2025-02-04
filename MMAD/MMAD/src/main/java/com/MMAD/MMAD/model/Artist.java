package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;

@Entity
public class Artist extends Item {

    public Artist() {
        super();
    }

    public Artist(String sourceId, String name) {
        super(sourceId, name);
    }

    public Artist(String imageURL, String sourceId, String name) {
        super(imageURL, sourceId, name);
    }

    public Artist(int id, String sourceId, String name) {
        super(id, sourceId, name);
    }

    public void setID(int id) {
        this.id = id;
    }
}