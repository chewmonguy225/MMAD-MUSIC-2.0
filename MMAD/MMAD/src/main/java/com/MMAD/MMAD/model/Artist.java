package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;

@Entity
public class Artist extends Item {

    public Artist() {
        super();
    }

    public Artist(String source_id, String name) {
        super(source_id, name);
    }

    public Artist(String imageURL, String source_id, String name) {
        super(imageURL, source_id, name);
    }

    public Artist(int id, String source_id, String name) {
        super(id, source_id, name);
    }

    public void setID(int id) {
        this.id = id;
    }
}