package com.MMAD.MMAD.service.item;

import com.MMAD.MMAD.model.Item.Song.Song;
import com.MMAD.MMAD.model.Item.Song.SongDTO;
import com.MMAD.MMAD.model.Item.Artist.Artist;
import com.MMAD.MMAD.model.Item.Artist.ArtistDTO;
import com.MMAD.MMAD.model.Item.Album.Album;
import com.MMAD.MMAD.model.Item.Album.AlbumDTO;
import com.MMAD.MMAD.repo.item.SongRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SongService {

    private final SongRepo songRepo;
    private final ArtistService artistService;
    private final AlbumService albumService;

    public SongService(SongRepo songRepo, ArtistService artistService, AlbumService albumService) {
        this.songRepo = songRepo;
        this.artistService = artistService;
        this.albumService = albumService;
    }

    // CREATE
    public SongDTO addSong(Song song) {
        if (song.getSourceId() == null || song.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be null or empty.");
        }

        Optional<Song> existingSongOpt = songRepo.findBySourceId(song.getSourceId());

        // Save/update Artists and Album first
        List<Artist> artistEntities = song.getArtists().stream()
            .map(artist -> ArtistDTO.toEntity(artistService.addArtist(artist)))
            .collect(Collectors.toList());

        Album album = AlbumDTO.toEntity(albumService.addAlbum(song.getAlbum()));

        Song savedSong;

        if (existingSongOpt.isPresent()) {
            Song existing = existingSongOpt.get();
            existing.setName(song.getName());
            existing.setImageURL(song.getImageURL());
            existing.setArtists(artistEntities);
            existing.setAlbum(album);
            savedSong = songRepo.save(existing);
        } else {
            song.setArtists(artistEntities);
            song.setAlbum(album);
            savedSong = songRepo.save(song);
        }

        return SongDTO.fromEntity(savedSong);
    }

    // READ
    @Transactional(readOnly = true)
    public Optional<SongDTO> getSongById(Long id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Invalid song ID.");

        return songRepo.findByIdWithAlbumAndArtist(id)
                .map(SongDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<SongDTO> getSongBySourceId(String sourceId) {
        if (sourceId == null || sourceId.trim().isEmpty())
            throw new IllegalArgumentException("Source ID cannot be null or empty.");

        return songRepo.findBySourceIdWithAlbumAndArtists(sourceId)
                .map(SongDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<SongDTO> getAllSongs() {
        return songRepo.findAllWithAlbumAndArtist()
                .stream()
                .map(SongDTO::fromEntity)
                .toList();
    }

    // UPDATE
    @Transactional
    public SongDTO updateSong(Long id, Song updatedDetails) {
        if (id == null || id <= 0 || updatedDetails == null)
            throw new IllegalArgumentException("Invalid input for update.");

        Song existing = songRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with ID: " + id));

        if (!existing.getSourceId().equals(updatedDetails.getSourceId())) {
            Optional<Song> conflict = songRepo.findBySourceId(updatedDetails.getSourceId());
            if (conflict.isPresent() && !conflict.get().getId().equals(id)) {
                throw new RuntimeException("Another song already exists with source ID: " + updatedDetails.getSourceId());
            }
        }

        existing.setName(updatedDetails.getName());
        existing.setSourceId(updatedDetails.getSourceId());

        if (!"default_image_url".equals(updatedDetails.getImageURL())) {
            existing.setImageURL(updatedDetails.getImageURL());
        }

        // Process artists
        List<Artist> updatedArtists = updatedDetails.getArtists().stream()
                .map(artist -> ArtistDTO.toEntity(artistService.addArtist(artist)))
                .collect(Collectors.toList());

        Album updatedAlbum = AlbumDTO.toEntity(albumService.addAlbum(updatedDetails.getAlbum()));

        existing.setArtists(updatedArtists);
        existing.setAlbum(updatedAlbum);

        return SongDTO.fromEntity(songRepo.save(existing));
    }

    // DELETE
    @Transactional
    public void deleteSong(Long id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Invalid song ID.");

        if (!songRepo.existsById(id)) {
            throw new EntityNotFoundException("Song not found with ID: " + id);
        }

        songRepo.deleteById(id);
    }
}
