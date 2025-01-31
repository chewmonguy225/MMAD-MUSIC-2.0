package com.MMAD.MMAD.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Item {

    @Column(nullable = false, updatable = false)
    protected String sourceID;

    protected String name;

    protected String imageURL;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;


    /**
     * Empty constructor for JPA.
     */
    public Item(){}


    
    /**
     * Constructor taking all parameters. id is generated automatically.
     * 
     * @param sourceID The item's source ID (spotify).
     * @param name The item's name.
     */
    public Item(String sourceID, String name) {
        this.sourceID = sourceID;
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

}
