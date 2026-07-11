package com.MMAD.dto.search;

import java.util.List;

import com.MMAD.dto.item.AlbumDTO;
import com.MMAD.dto.item.ArtistDTO;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.dto.item.SongDTO;
import com.MMAD.model.item.MusicProvider;

public class ItemSearchResultDTO extends SearchResultDTO {

    private String sourceId;
    private MusicProvider provider;
    private List<String> artists;

    public ItemSearchResultDTO(
            Long id,
            String sourceId,
            MusicProvider provider,
            String name,
            String imageURL,
            String type,
            List<String> artists) {

        super(id, name, imageURL, type);

        this.sourceId = sourceId;
        this.provider = provider;
        this.artists = artists;
    }

    public String getSourceId() {
        return sourceId;
    }

    public MusicProvider getProvider() {
        return provider;
    }

    public List<String> getArtists() {
        return artists;
    }

    public static ItemSearchResultDTO fromDTO(ItemDTO item) {

        List<String> artists = List.of();

        String type = item.getClass()
                .getSimpleName()
                .replace("DTO", "")
                .toLowerCase();

        if (item instanceof AlbumDTO album) {

            if (album.getArtists() != null) {
                artists = album.getArtists()
                        .stream()
                        .map(ArtistDTO::getName)
                        .toList();
            }

        } else if (item instanceof SongDTO song) {

            if (song.getArtists() != null) {
                artists = song.getArtists()
                        .stream()
                        .map(ArtistDTO::getName)
                        .toList();
            }

        } else if (item instanceof ArtistDTO artist) {

            artists = List.of(artist.getName());

        }

        return new ItemSearchResultDTO(
                item.getId(),
                item.getSourceId(),
                item.getProvider(),
                item.getName(),
                item.getImageURL(),
                type,
                artists);
    }
}