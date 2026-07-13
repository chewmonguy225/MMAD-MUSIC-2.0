package com.MMAD.dto.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.MMAD.entity.item.Album;
import com.MMAD.entity.item.Artist;
import com.MMAD.entity.item.MusicProvider;
import com.MMAD.entity.item.Song;

public class SongDTO extends ItemDTO {

    private List<ArtistDTO> artists;
    private AlbumDTO album;

    private String releaseDate;
    private Integer durationMs;

    public SongDTO() {
        super();
    }

    public SongDTO(
            Long id,
            String sourceId,
            MusicProvider provider,
            String name,
            String imageURL,
            List<ArtistDTO> artists,
            AlbumDTO album,
            String releaseDate,
            Integer durationMs) {

        super(id, sourceId, provider, name, imageURL);

        this.artists = artists;
        this.album = album;
        this.releaseDate = releaseDate;
        this.durationMs = durationMs;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
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
                song.getProvider(),
                song.getName(),
                song.getImageURL(),
                artistDTOs,
                AlbumDTO.fromEntity(song.getAlbum()),
                song.getReleaseDate(),
                song.getDurationMs());
    }

    @Override
    public Song toEntity() {

        List<Artist> artistEntities = new ArrayList<>();

        if (this.getArtists() != null) {

            artistEntities = this.getArtists()
                    .stream()
                    .map(ArtistDTO::toEntity)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        Album albumEntity = null;

        if (this.getAlbum() != null) {
            albumEntity = this.getAlbum().toEntity();
        }

        return new Song(
                this.getSourceId(),
                this.getProvider(),
                this.getName(),
                this.getImageURL(),
                artistEntities,
                albumEntity,
                this.getReleaseDate(),
                this.getDurationMs());
    }
}