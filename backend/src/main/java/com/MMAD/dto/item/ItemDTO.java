package com.MMAD.dto.item;

import com.MMAD.entity.item.Album;
import com.MMAD.entity.item.Artist;
import com.MMAD.entity.item.Item;
import com.MMAD.entity.item.MusicProvider;
import com.MMAD.entity.item.Song;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArtistDTO.class, name = "artist"),
        @JsonSubTypes.Type(value = AlbumDTO.class, name = "album"),
        @JsonSubTypes.Type(value = SongDTO.class, name = "song")
})
public abstract class ItemDTO {

    private Long id;
    private String sourceId;
    private MusicProvider provider;
    private String name;
    private String imageURL;

    private int relevance;

    public ItemDTO() {
    }

    public ItemDTO(Long id, String sourceId, MusicProvider provider, String name, String imageURL) {
        this.id = id;
        this.sourceId = sourceId;
        this.provider = provider;
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

    public MusicProvider getProvider() {
        return provider;
    }

    public void setProvider(MusicProvider provider) {
        this.provider = provider;
    }

    // =========================
    // Polymorphic mapping
    // =========================
    public abstract Item toEntity();

    // =========================
    // Polymorphic mapping
    // =========================

    public static ItemDTO fromEntity(Item item) {

        if (item == null) {
            return null;
        }

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
                "Unsupported Item type: " + item.getClass());
    }
}