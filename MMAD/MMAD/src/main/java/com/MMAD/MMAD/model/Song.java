package com.MMAD.MMAD.model;

@Entity
public class Song extends Item{
    private Album album;
    private Artist artist;

    public Song(String sourceID, String name, Artist artist, Album album){
        super(sourceID, name);
        this.artist = artist;
        this.album = album;
    }
    public Song(int id,String sourceID, String name, Artist artist, Album album){
        super(id, sourceID, name);
        this.artist = artist;
        this.album = album;
    }

    public void setID(int id){
        this.id = id;
    }

    public Album getAlbum(){
        return this.album;
    }

    public Artist getArtist(){
        return this.artist;
    }
}