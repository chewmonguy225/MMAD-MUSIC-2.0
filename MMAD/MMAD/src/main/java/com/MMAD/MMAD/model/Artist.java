package com.MMAD.MMAD.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "artist")
public class Artist extends Item {
    // Define relationships if any
    // @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    // private List<Album> albums;

    public Artist() {
        super();
    }

    public Artist(String sourceId, String name) {
        super(sourceId, name);
    }

    public Artist(String imageURL, String sourceId, String name) {
        super(imageURL, sourceId, name);
    }

    public Artist(Long id, String sourceId, String name) {
        super(id, sourceId, name);
    }

    public void setID(Long id) {
        this.id = id;
    }
}