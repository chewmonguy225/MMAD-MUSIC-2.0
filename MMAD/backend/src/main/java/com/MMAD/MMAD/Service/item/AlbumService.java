package com.MMAD.MMAD.service.item;

import com.MMAD.MMAD.model.Item.Album.Album;
import com.MMAD.MMAD.model.Item.Artist.Artist;
import com.MMAD.MMAD.model.Item.Artist.ArtistDTO;
import com.MMAD.MMAD.model.Item.Album.AlbumDTO;
import com.MMAD.MMAD.repo.item.AlbumRepo;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepo albumRepo;
    private final ArtistService artistService;

    public AlbumService(AlbumRepo albumRepo, ArtistService artistService) {
        this.albumRepo = albumRepo;
        this.artistService = artistService;
    }

    public AlbumDTO addAlbum(Album album) {
        if (album.getSourceId() == null || album.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be empty or null for a new album.");
        }

        Optional<Album> existingAlbumOpt = albumRepo.findBySourceId(album.getSourceId());

        Album savedAlbum;

        if (existingAlbumOpt.isPresent()) {
            // Update existing album
            Album existingAlbum = existingAlbumOpt.get();

            existingAlbum.setName(album.getName());
            existingAlbum.setImageURL(album.getImageURL());

            // Step 1: Save/update artists and get DTOs
            List<ArtistDTO> artistDTOs = album.getArtists().stream()
                    .map(artist -> artistService.addArtist(artist))
                    .collect(Collectors.toList());

            // Step 2: Convert DTOs back to entities (mutable list!)
            List<Artist> managedArtists = artistDTOs.stream()
                    .map(dto -> ArtistDTO.toEntity(dto))
                    .collect(Collectors.toCollection(ArrayList::new));

            existingAlbum.setArtists(managedArtists);

            savedAlbum = albumRepo.save(existingAlbum);
        } else {
            // For new album, do the same with artists
            List<ArtistDTO> artistDTOs = album.getArtists().stream()
                    .map(artist -> artistService.addArtist(artist))
                    .collect(Collectors.toList());

            List<Artist> managedArtists = artistDTOs.stream()
                    .map(dto -> ArtistDTO.toEntity(dto))
                    .collect(Collectors.toCollection(ArrayList::new));

            album.setArtists(managedArtists);

            savedAlbum = albumRepo.save(album);
        }

        return AlbumDTO.fromEntity(savedAlbum);
    }

    @Transactional(readOnly = true)
    public Optional<AlbumDTO> getAlbumById(Long id) {
        Optional<Album> albumFound = albumRepo.findByIdWithArtists(id);
        return albumFound.map(AlbumDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<AlbumDTO> getAlbumBySourceId(String sourceId) {
        Optional<Album> albumFound = albumRepo.findBySourceIdWithArtists(sourceId);
        return albumFound.map(AlbumDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<AlbumDTO> getAllAlbums() {
        List<Album> albums = albumRepo.findAllWithArtists();
        return albums.stream()
                .map(AlbumDTO::fromEntity)
                .toList();
    }

    @Transactional
    public AlbumDTO updateAlbum(Long id, Album updatedAlbumDetails) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Album ID cannot be null or non-positive for update.");
        }
        if (updatedAlbumDetails == null) {
            throw new IllegalArgumentException("Updated album details cannot be null.");
        }
        if (updatedAlbumDetails.getSourceId() == null || updatedAlbumDetails.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be empty or null in updated album details.");
        }

        Album existingAlbum = albumRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album with ID " + id + " not found for update."));

        if (!existingAlbum.getSourceId().equals(updatedAlbumDetails.getSourceId())) {
            Optional<Album> duplicateSourceIdAlbum = albumRepo.findBySourceId(updatedAlbumDetails.getSourceId());

            if (duplicateSourceIdAlbum.isPresent() && !duplicateSourceIdAlbum.get().getId().equals(id)) {
                throw new RuntimeException("Another album already exists with source ID '"
                        + updatedAlbumDetails.getSourceId() + "'.");
            }
        }

        existingAlbum.setName(updatedAlbumDetails.getName());
        existingAlbum.setImageURL(updatedAlbumDetails.getImageURL());
        existingAlbum.setSourceId(updatedAlbumDetails.getSourceId());

        return AlbumDTO.fromEntity(albumRepo.save(existingAlbum));
    }

    @Transactional
    public void deleteAlbum(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Album ID cannot be null or non-positive for deletion.");
        }
        if (!albumRepo.existsById(id)) {
            throw new EntityNotFoundException("Album not found with ID: " + id);
        }
        albumRepo.deleteById(id);
    }
    
}
