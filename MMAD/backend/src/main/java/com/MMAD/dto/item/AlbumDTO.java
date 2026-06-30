package com.MMAD.dto.item;

import com.MMAD.model.item.Album;

import java.util.List;

public class AlbumDTO extends ItemDTO {

    private List<ArtistDTO> artists;

    public AlbumDTO() {
        super();
    }

    public AlbumDTO(
            Long id,
            String sourceId,
            String name,
            String imageURL,
            List<ArtistDTO> artists
    ) {
        super(id, sourceId, name, imageURL);
        this.artists = artists;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public static AlbumDTO fromEntity(Album album) {
        if (album == null) return null;

        return new AlbumDTO(
                album.getId(),
                album.getSourceId(),
                album.getName(),
                album.getImageURL(),
                album.getArtists()
                        .stream()
                        .map(ArtistDTO::fromEntity)
                        .toList()
        );
    }

    public static Album toEntity(AlbumDTO dto) {
        if (dto == null) return null;

        Album album = new Album(
                dto.getImageURL(),
                dto.getSourceId(),
                dto.getName(),
                dto.getArtists()
                        .stream()
                        .map(ArtistDTO::toEntity)
                        .toList()
        );

        album.setId(dto.getId());

        return album;
    }
}