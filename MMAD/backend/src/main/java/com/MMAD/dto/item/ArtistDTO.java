package com.MMAD.dto.item;

import com.MMAD.model.item.Artist;
import com.MMAD.model.item.MusicProvider;

public class ArtistDTO extends ItemDTO {

    public ArtistDTO() {
        super();
    }

    public ArtistDTO(Long id, String sourceId, MusicProvider provider, String name, String imageURL) {
        super(id, sourceId, provider, name, imageURL);
    }

    public static ArtistDTO fromEntity(Artist artist) {
        if (artist == null)
            return null;

        return new ArtistDTO(
                artist.getId(),
                artist.getSourceId(),
                artist.getProvider(),
                artist.getName(),
                artist.getImageURL());
    }

    @Override
    public Artist toEntity() {
        return new Artist(
                this.getSourceId(),
                this.getProvider(),
                this.getName(),
                this.getImageURL());
    }
}
