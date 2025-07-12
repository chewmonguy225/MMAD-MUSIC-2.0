package com.MMAD.MMAD.model.Item.Album;

import com.MMAD.MMAD.model.Item.ItemDTO;
import com.MMAD.MMAD.model.Item.Artist.ArtistDTO;

import java.util.List;
import java.util.stream.Collectors;

public class AlbumDTO extends ItemDTO {

    private List<ArtistDTO> artists;

    // Constructors
    public AlbumDTO() {
        super();
    }

    public AlbumDTO(Long id, String sourceId, String name, String imageURL, List<ArtistDTO> artists) {
        super(id, sourceId, name, imageURL);
        this.artists = artists;
    }

    // Getter & Setter
    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    // Static mapper from Album entity to AlbumDTO
    public static AlbumDTO fromEntity(Album album) {
        if (album == null)
            return null;

        return new AlbumDTO(
                album.getId(),
                album.getSourceId(),
                album.getName(),
                album.getImageURL(),
                album.getArtists() == null ? null :
                        album.getArtists()
                             .stream()
                             .map(ArtistDTO::fromEntity)
                             .collect(Collectors.toList())
        );
    }
}
