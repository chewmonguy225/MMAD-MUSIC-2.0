// package com.MMAD.MMAD.model.Item;

// import jakarta.persistence.DiscriminatorValue;
// import jakarta.persistence.Entity;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "songs")
// @DiscriminatorValue("song")
// public class Song extends Item {

//     @ManyToOne
//     @JoinColumn(name = "album_item_id", referencedColumnName = "id")
//     private Album album;

//     @ManyToOne
//     @JoinColumn(name = "artist_item_id", referencedColumnName = "id")
//     private Artist artist;

//     public Song(){
//         super();
//     }

//     public Song(String sourceId, String name, Artist artist, Album album){
//         super(sourceId, name);
//         this.artist = artist;
//         this.album = album;
//     }

//     public Song(String imageURL, String sourceId, String name, Artist artist, Album album){
//         super(imageURL, sourceId, name);
//         this.artist = artist;
//         this.album = album;
//     }

//     public Album getAlbum(){
//         return this.album;
//     }

//     public void setAlbum(Album album){
//         this.album = album;
//     }

//     public Artist getArtist(){
//         return this.artist;
//     }

//     public void setArtist(Artist artist){
//         this.artist = artist;
//     }

//     @Override
//     public String toString() {
//         return "Song{" +
//                 "id=" + getId() +
//                 ", sourceId='" + getSourceId() + '\'' +
//                 ", name='" + getName() + '\'' +
//                 ", imageURL='" + getImageURL() + '\'' +
//                 ", album=" + (album != null ? album.getName() : "N/A") +
//                 ", artist=" + (artist != null ? artist.getName() : "N/A") +
//                 '}';
//     }
// }