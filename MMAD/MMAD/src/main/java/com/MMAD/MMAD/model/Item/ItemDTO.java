// src/main/java/com/MMAD/MMAD/model/Item/ItemDTO.java
package com.MMAD.MMAD.model.Item;

import com.MMAD.MMAD.model.Item.Album.Album;
import com.MMAD.MMAD.model.Item.Album.AlbumDTO;
import com.MMAD.MMAD.model.Item.Artist.Artist;
import com.MMAD.MMAD.model.Item.Artist.ArtistDTO;
import com.MMAD.MMAD.model.Item.Song.Song;
import com.MMAD.MMAD.model.Item.Song.SongDTO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDateTime; // Make sure this is imported if used in entities

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArtistDTO.class, name = "ARTIST"),
        @JsonSubTypes.Type(value = AlbumDTO.class, name = "ALBUM"),
        @JsonSubTypes.Type(value = SongDTO.class, name = "SONG")
})
public abstract class ItemDTO { // Correct: it is abstract

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
        if (item == null) {
            return null;
        }

        // Use instanceof to determine the actual concrete type of the 'item' entity
        // and then call the corresponding specific DTO's fromEntity method.
        if (item instanceof Artist) {
            return ArtistDTO.fromEntity((Artist) item);
        }
    
        else if (item instanceof Album) {
        return AlbumDTO.fromEntity((Album) item);
        }
        else if (item instanceof Song) {
        return SongDTO.fromEntity((Song) item);
        }
        else {
            // This 'else' block means an item entity type was found that isn't
            // mapped to a specific DTO. This should ideally not happen if
            // all possible Item entity types are covered.
            System.err.println("Unsupported Item entity type for DTO mapping: " + item.getClass().getName());
            // Since ItemDTO is abstract, we can't return new ItemDTO().
            // You should typically throw an exception here, as it indicates
            // an unhandled case in your data model.
            throw new IllegalArgumentException(
                    "Unsupported item type encountered during DTO mapping: " + item.getClass().getName());
        }
    }
}