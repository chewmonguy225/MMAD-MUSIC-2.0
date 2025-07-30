package com.MMAD.MMAD.model.Item.Album;

import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.model.Item.Artist.Artist;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "albums")
@PrimaryKeyJoinColumn(name = "id") // Ensures id matches with Item table (JOINED inheritance)
public class Album extends Item {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "album_artists", // Join table name
        joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id")
    )
    //@JsonIgnore // Prevent infinite recursion during serialization
    private List<Artist> artists = new ArrayList<>();

    // Constructors
    public Album() {
        super();
    }

    public Album(String sourceId, String name, List<Artist> artists) {
        super(sourceId, name, null);
        this.artists = artists;
    }

    public Album(String imageURL, String sourceId, String name, List<Artist> artists) {
        super(sourceId, name, imageURL);
        this.artists = artists;
    }

    // Getter and Setter
    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    // toString()
    @Override
    public String toString() {
        return "Album{" +
                "id=" + getId() +
                ", sourceId='" + getSourceId() + '\'' +
                ", name='" + getName() + '\'' +
                ", imageURL='" + getImageURL() + '\'' +
                ", artists=" + (artists != null ? artists.stream().map(Artist::getName).toList() : "N/A") +
                '}';
    }
}
