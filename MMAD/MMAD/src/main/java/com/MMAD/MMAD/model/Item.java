package com.MMAD.MMAD.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Item {
    protected String imageURL;
    
    //@Column(nullable = false, updatable = false)
    protected String source_id;
    
    protected String name;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;

    public Item() {}

    public Item(String source_id, String name) {
        this.source_id = source_id;
        this.name = name;
    }

    public Item(String imageURL, String source_id, String name) {
        this.imageURL = imageURL;
        this.source_id = source_id;
        this.name = name;
    }

    public Item(int id, String source_id, String name) {
        this.id = id;
        this.source_id = source_id;
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}