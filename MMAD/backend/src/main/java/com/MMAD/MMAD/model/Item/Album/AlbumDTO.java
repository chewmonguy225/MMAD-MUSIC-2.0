package com.MMAD.MMAD.model.Item.Album;

import com.MMAD.MMAD.model.Item.ItemDTO;
import com.MMAD.MMAD.model.Item.Artist.Artist;
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

    // Getters & Setters
    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    // From Album entity to DTO
    public static AlbumDTO fromEntity(Album album) {
        if (album == null) return null;

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

    public static Album toEntity(AlbumDTO dto) {
        if (dto == null) return null;
    
        // Convert ArtistDTOs to Artist entities using explicit lambda to avoid ambiguity
        List<Artist> artistEntities = dto.getArtists() == null ? null :
            dto.getArtists()
               .stream()
               .map(dtoArtist -> ArtistDTO.toEntity(dtoArtist))
               .collect(Collectors.toList());
    
        // Use the constructor that includes imageURL
        Album album = new Album(
            dto.getImageURL(),
            dto.getSourceId(),
            dto.getName(),
            artistEntities
        );
    
        album.setId(dto.getId()); // Set ID for updates
        return album;
    }    
    
}
