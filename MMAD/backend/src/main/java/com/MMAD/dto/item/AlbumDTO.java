package com.MMAD.dto.item;

import com.MMAD.model.item.Album;
import com.MMAD.model.item.Artist;
import com.MMAD.model.item.MusicProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumDTO extends ItemDTO {

    private List<ArtistDTO> artists;

    public AlbumDTO() {
        super();
    }

    public AlbumDTO(
            Long id,
            String sourceId,
            MusicProvider provider,
            String name,
            String imageURL,
            List<ArtistDTO> artists) {
        super(id, sourceId, provider, name, imageURL);
        this.artists = artists;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public static AlbumDTO fromEntity(Album album) {
        if (album == null)
            return null;

        return new AlbumDTO(
                album.getId(),
                album.getSourceId(),
                album.getProvider(),
                album.getName(),
                album.getImageURL(),
                album.getArtists()
                        .stream()
                        .map(ArtistDTO::fromEntity)
                        .toList());
    }

    @Override
    public Album toEntity() {

        List<Artist> artistEntities = new ArrayList<>();

        if (this.getArtists() != null) {
            artistEntities = this.getArtists()
                    .stream()
                    .map(ArtistDTO::toEntity)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new Album(
                this.getSourceId(),
                this.getProvider(),
                this.getName(),
                artistEntities,
                this.getImageURL());
    }
}