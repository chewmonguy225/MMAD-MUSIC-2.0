package com.MMAD.MMAD.model;
public class Album extends Item {
    private Artist artist;

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
    
    @Override
    public String getName(){
        return this.name;
    }
    @Override
    public String getSourceID(){
        return this.sourceID;
    }
    @Override
    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public Artist getArtist(){
        return this.artist;
    }

}
