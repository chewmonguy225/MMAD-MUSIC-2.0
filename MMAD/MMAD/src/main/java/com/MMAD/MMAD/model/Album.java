package com.MMAD.MMAD.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "album")
public class Album extends Item { 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Artist artist;

    public Album(){super();}

    public Album(String sourceID, String name, Artist artist){
        super(sourceID, name);
        this.artist = artist;
    }

    public Album(int id, String sourceID, String name, Artist artist){
        super(id, sourceID, name);
        this.artist = artist;
    }

    public Artist artist(){
        return this.artist;
    }

    public void setID(int id){
        this.id = id;
    }

    public Artist getArtist(){
        return this.artist;
    }

}
