package com.MMAD.dto.item;

import com.MMAD.model.item.Album;
import com.MMAD.model.item.Artist;
import com.MMAD.model.item.Item;
import com.MMAD.model.item.Song;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
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

    // ⭐ NEW: relevance for sorting search results
    private int relevance;

    public ItemDTO() {}

    public ItemDTO(Long id, String sourceId, String name, String imageURL) {
        this.id = id;
        this.sourceId = sourceId;
        this.name = name;
        this.imageURL = imageURL;
    }

    // =========================
    // Getters / Setters
    // =========================

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

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    // =========================
    // Polymorphic mapping
    // =========================

    public static ItemDTO fromEntity(Item item) {

        if (item == null) return null;

        if (item instanceof Artist artist) {
            return ArtistDTO.fromEntity(artist);
        }

        if (item instanceof Album album) {
            return AlbumDTO.fromEntity(album);
        }

        if (item instanceof Song song) {
            return SongDTO.fromEntity(song);
        }

        throw new IllegalArgumentException(
                "Unsupported Item type: " + item.getClass()
        );
    }
}