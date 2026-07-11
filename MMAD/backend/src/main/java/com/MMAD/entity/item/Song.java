package com.MMAD.entity.item;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "songs")
@DiscriminatorValue("song")
public class Song extends Item {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_item_id", referencedColumnName = "id")
    private Album album;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "song_artists", joinColumns = @JoinColumn(name = "song_item_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "artist_item_id", referencedColumnName = "id"))
    private List<Artist> artists = new ArrayList<>();

    public Song() {
        super();
    }

    public Song(String sourceId, MusicProvider provider, String name, List<Artist> artists, Album album) {
        super(sourceId, provider, name, "default_image_url"); // Use default image if none provided
        this.artists = artists;
        this.album = album;
    }

    public Song(String sourceId, MusicProvider provider, String name, String imageURL, List<Artist> artists,
            Album album) {
        super(sourceId, provider, name, imageURL);
        this.artists = artists;
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + getId() +
                ", sourceId='" + getSourceId() + '\'' +
                ", name='" + getName() + '\'' +
                ", imageURL='" + getImageURL() + '\'' +
                ", album=" + (album != null ? album.getName() : "N/A") +
                ", artists=" + (artists != null ? artists.stream().map(Artist::getName).toList() : "[]") +
                '}';
    }
}
