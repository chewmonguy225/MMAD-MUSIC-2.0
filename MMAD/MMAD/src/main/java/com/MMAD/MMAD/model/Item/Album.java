// package com.MMAD.MMAD.model.Item;

// import jakarta.persistence.DiscriminatorValue;
// import jakarta.persistence.Entity;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "albums")
// @DiscriminatorValue("album")
// public class Album extends Item {

//     @ManyToOne
//     @JoinColumn(name = "artist_item_id", referencedColumnName = "id", nullable = false)
//     private Artist artist;

//     public Album(){
//         super();
//     }

//     public Album(String sourceId, String name, Artist artist){
//         super(sourceId, name);
//         this.artist = artist;
//     }

//     public Album(String imageURL, String sourceId, String name, Artist artist){
//         super(imageURL, sourceId, name);
//         this.artist = artist;
//     }

//     public Artist getArtist(){
//         return this.artist;
//     }

//     public void setArtist(Artist artist){
//         this.artist = artist;
//     }

//     @Override
//     public String toString() {
//         return "Album{" +
//                 "id=" + getId() +
//                 ", sourceId='" + getSourceId() + '\'' +
//                 ", name='" + getName() + '\'' +
//                 ", imageURL='" + getImageURL() + '\'' +
//                 ", artist=" + (artist != null ? artist.getName() : "N/A") +
//                 '}';
//     }
// }