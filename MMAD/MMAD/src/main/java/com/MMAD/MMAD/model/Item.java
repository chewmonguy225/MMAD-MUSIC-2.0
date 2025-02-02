package com.MMAD.MMAD.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    protected int id;
    protected String source_id;
    protected String name;
    @Column(nullable = true)
    protected String imageURL;
   
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
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
            ", source_id="+source_id+'\''+
            ", name="+name+'\''+
            ", imageURL="+imageURL+'\''+
            '}';
    }
    
}