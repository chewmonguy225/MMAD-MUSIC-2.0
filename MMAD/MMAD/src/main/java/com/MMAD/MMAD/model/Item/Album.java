package com.MMAD.MMAD.model.Item;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "album")
@DiscriminatorValue("ALBUM")
public class Album extends Item { 
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "artist_id", nullable = false)
    //@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @Column(name = "artist_id", nullable = false)
    private Long artistID;

    public Album(){super();}

    public Album(String sourceID, String name, Long artistID){
        super(sourceID, name);
        this.artistID = artistID;
    }

    public Album(Long id, String sourceID, String name, Long artistID){
        super(id, sourceID, name);
        this.artistID = artistID;
    }

    public Long artist(){
        return this.artistID;
    }

    public void setID(Long id){
        this.id = id;
    }

    public Long getArtist(){
        return this.artistID;
    }

}
