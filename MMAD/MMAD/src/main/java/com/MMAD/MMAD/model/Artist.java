package com.MMAD.MMAD.model;

public class Artist extends Item {

    public Artist(String sourceID, String name){
        super(sourceID, name);
    }
    public Artist(int id, String sourceID, String name){
        super(id, sourceID, name);
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
}
