package com.MMAD.dto.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.MMAD.entity.item.Album;
import com.MMAD.entity.item.Artist;
import com.MMAD.entity.item.MusicProvider;

public class AlbumDTO extends ItemDTO {

    private List<ArtistDTO> artists;

    private String releaseDate;

    public AlbumDTO() {
        super();
    }

    public AlbumDTO(
            Long id,
            String sourceId,
            MusicProvider provider,
            String name,
            String imageURL,
            List<ArtistDTO> artists,
            String releaseDate) {

        super(id, sourceId, provider, name, imageURL);

        this.artists = artists;
        this.releaseDate = releaseDate;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public static AlbumDTO fromEntity(Album album) {

        if (album == null) {
            return null;
        }

        return new AlbumDTO(
                album.getId(),
                album.getSourceId(),
                album.getProvider(),
                album.getName(),
                album.getImageURL(),
                album.getArtists()
                        .stream()
                        .map(ArtistDTO::fromEntity)
                        .toList(),
                album.getReleaseDate());
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
                this.getImageURL(),
                this.getReleaseDate());
    }
}