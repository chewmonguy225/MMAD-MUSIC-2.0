package com.MMAD.MMAD.model;

import jakarta.persistence.*;

@Entity
public abstract class Item {
    @Column(nullable = false, updatable = false)
    protected String sourceID;
    protected String name;
    protected String imageURL;
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;

    public Item(){}

    public Item(String sourceID, String name) {
        this.sourceID = sourceID;
        this.id = -1;
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item(int id, String sourceID, String name) {
        this.id = id;
        this.sourceID = sourceID;
        this.name = name;
    }
}
