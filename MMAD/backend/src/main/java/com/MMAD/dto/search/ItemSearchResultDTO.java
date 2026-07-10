package com.MMAD.dto.search;

import java.util.List;

import com.MMAD.model.item.Album;
import com.MMAD.model.item.Artist;
import com.MMAD.model.item.Item;
import com.MMAD.model.item.Song;

public class ItemSearchResultDTO extends SearchResultDTO {

    private List<String> artists;


    public ItemSearchResultDTO(
            Long id,
            String name,
            String imageURL,
            String type,
            List<String> artists
    ) {
        super(id, name, imageURL, type);
        this.artists = artists;
    }


    public List<String> getArtists() {
        return artists;
    }


    public static ItemSearchResultDTO fromEntity(Item item) {

        List<String> artists = List.of();

        if (item instanceof Album album) {
            artists = album.getArtists()
                    .stream()
                    .map(Artist::getName)
                    .toList();
        }

        if (item instanceof Song song) {
            artists = song.getArtists()
                    .stream()
                    .map(Artist::getName)
                    .toList();
        }


        String type = item.getClass()
                .getSimpleName()
                .toLowerCase();


        return new ItemSearchResultDTO(
                item.getId(),
                item.getName(),
                item.getImageURL(),
                type,
                artists
        );
    }
}