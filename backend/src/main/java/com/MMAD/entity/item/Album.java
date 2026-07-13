package com.MMAD.entity.item;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "albums")
@DiscriminatorValue("album")
@PrimaryKeyJoinColumn(name = "id") // Ensures id matches with Item table (JOINED inheritance)
public class Album extends Item {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "album_artists", // Join table name
            joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"))
    // @JsonIgnore // Prevent infinite recursion during serialization
    private List<Artist> artists = new ArrayList<>();

    private String releaseDate;

    // Constructors
    public Album() {
        super();
    }

    public Album(String sourceId, MusicProvider provider, String name, List<Artist> artists) {
        super(sourceId, provider, name, null);
        this.artists = artists;
    }

    public Album(String sourceId, MusicProvider provider, String name, List<Artist> artists, String imageURL) {
        super(sourceId, provider, name, imageURL);
        this.artists = artists;
    }

    public Album(String sourceId, MusicProvider provider, String name, List<Artist> artists, String imageURL, String releaseDate) {
        super(sourceId, provider, name, imageURL);
        this.artists = artists;
        this.releaseDate = releaseDate;
    }

    // Getter and Setter
    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    // toString()
    @Override
    public String toString() {
        return "Album{" +
                "id=" + getId() +
                ", sourceId='" + getSourceId() + '\'' +
                ", name='" + getName() + '\'' +
                ", imageURL='" + getImageURL() + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", artists=" + (artists != null ? artists.stream().map(Artist::getName).toList() : "N/A") +
                '}';
    }
}