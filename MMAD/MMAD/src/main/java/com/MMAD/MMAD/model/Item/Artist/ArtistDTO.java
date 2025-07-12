package com.MMAD.MMAD.model.Item.Artist;
import com.MMAD.MMAD.model.Item.ItemDTO;

public class ArtistDTO extends ItemDTO {

    public ArtistDTO() {
        super();
    }

    public ArtistDTO(Long id, String sourceId, String name, String imageURL) {
        super(id, sourceId, name, imageURL);
    }

    public static ArtistDTO fromEntity(Artist artist) {
        if (artist == null) return null;

        return new ArtistDTO(
            artist.getId(),
            artist.getSourceId(),
            artist.getName(),
            artist.getImageURL()
        );
    }

    public static Artist toEntity(ArtistDTO dto) {
        if (dto == null) return null;

        Artist artist = new Artist(
            dto.getSourceId(),
            dto.getName(),
            dto.getImageURL()
        );
        artist.setId(dto.getId()); // for updates
        return artist;
    }
}
