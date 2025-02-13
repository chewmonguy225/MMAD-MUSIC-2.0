package com.MMAD.MMAD.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    protected int id;
    
    @Column(name = "source_id", nullable = false)
    protected String sourceId;
    
    protected String name;
    
    @Column(nullable = true)
    protected String imageURL = "https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png";

    public Item() {}

    public Item(String sourceId, String name) {
        this.sourceId = sourceId;
        this.name = name;
    }

    public Item(String imageURL, String sourceId, String name) {
        this.imageURL = imageURL;
        this.sourceId = sourceId;
        this.name = name;
    }

    public Item(int id, String sourceId, String name) {
        this.id = id;
        this.sourceId = sourceId;
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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return "Artist{"+
            "id="+id+
            ", sourceId="+sourceId+'\''+
            ", name="+name+'\''+
            ", imageURL="+imageURL+'\''+
            '}';
    }
    
}