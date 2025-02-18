package com.MMAD.MMAD.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
public class Artist extends Item {
    // Define relationships if any
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Album> albums;

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