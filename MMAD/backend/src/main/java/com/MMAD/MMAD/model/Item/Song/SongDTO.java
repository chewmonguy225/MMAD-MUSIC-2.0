package com.MMAD.MMAD.model.Item.Song;

import com.MMAD.MMAD.model.Item.Album.AlbumDTO;
import com.MMAD.MMAD.model.Item.Artist.Artist;
import com.MMAD.MMAD.model.Item.Artist.ArtistDTO;
import com.MMAD.MMAD.model.Item.ItemDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SongDTO extends ItemDTO {

    private List<ArtistDTO> artists;
    private AlbumDTO album;

    public SongDTO() {
        super();
    }

    public SongDTO(Long id, String sourceId, String name, String imageURL,
            List<ArtistDTO> artists, AlbumDTO album) {
        super(id, sourceId, name, imageURL);
        this.artists = artists;
        this.album = album;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public AlbumDTO getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDTO album) {
        this.album = album;
    }

    public static SongDTO fromEntity(Song song) {
        if (song == null)
            return null;

        List<ArtistDTO> artistDTOs = song.getArtists() == null ? null
                : song.getArtists()
                        .stream()
                        .map(ArtistDTO::fromEntity)
                        .collect(Collectors.toList());

        return new SongDTO(
                song.getId(),
                song.getSourceId(),
                song.getName(),
                song.getImageURL(),
                artistDTOs,
                AlbumDTO.fromEntity(song.getAlbum()));
    }

    public static Song toEntity(SongDTO dto) {
        if (dto == null)
            return null;

        List<Artist> artistEntities = dto.getArtists() == null ? null
                : dto.getArtists()
                        .stream()
                        .map(artistDto -> ArtistDTO.toEntity(artistDto))
                        .collect(Collectors.toList());

        Song song = new Song(
                dto.getImageURL(),
                dto.getSourceId(),
                dto.getName(),
                artistEntities,
                AlbumDTO.toEntity(dto.getAlbum()));

        song.setId(dto.getId());
        return song;
    }
}
