// src/main/java/com/MMAD/MMAD/model/Item/ItemDTO.java
package com.MMAD.dto.item;

import com.MMAD.model.item.Album;
import com.MMAD.model.item.Artist;
import com.MMAD.model.item.Item;
import com.MMAD.model.item.Song;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDateTime; // Make sure this is imported if used in entities

import org.hibernate.Hibernate;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArtistDTO.class, name = "artist"),
        @JsonSubTypes.Type(value = AlbumDTO.class, name = "album"),
        @JsonSubTypes.Type(value = SongDTO.class, name = "song")
})
public class ItemDTO {

    private Long id;
    private String sourceId;
    private String name;
    private String imageURL;

    public ItemDTO() {
    } // Default constructor for Jackson

    public ItemDTO(Long id, String sourceId, String name, String imageURL) {
        this.id = id;
        this.sourceId = sourceId;
        this.name = name;
        this.imageURL = imageURL;
    }

    // Getters and Setters (ensure they include createdAt and updatedAt)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    // *** THIS IS THE CRITICAL CHANGE FOR fromEntity ***
    // This method dispatches to the correct specific DTO's fromEntity method.
    public static ItemDTO fromEntity(Item item) {

        if (item == null)
            return null;

        if (item instanceof Artist artist) {
            return ArtistDTO.fromEntity(artist);
        }

        if (item instanceof Song song) {
            return SongDTO.fromEntity(song);
        }

        if (item instanceof Album album) {
            return AlbumDTO.fromEntity(album);
        }

        throw new IllegalArgumentException(
                "Unsupported Item type: " + item.getClass());
    }
}