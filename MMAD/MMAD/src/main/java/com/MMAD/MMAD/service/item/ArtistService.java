
package com.MMAD.MMAD.service.item; // <--- IMPORTANT: Changed package here

import com.MMAD.MMAD.model.Item.Artist.Artist;
import com.MMAD.MMAD.model.Item.Artist.ArtistDTO;
import com.MMAD.MMAD.repo.item.ArtistRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    private final ArtistRepo artistRepo;

    public ArtistService(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }

    @Transactional
    public ArtistDTO addArtist(Artist artist) {
        // 1: Validate that sourceId is provided
        if (artist.getSourceId() == null || artist.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be empty or null for a new artist.");
        }

        // 2: Check if an artist with the given sourceId already exists
        Optional<Artist> existingArtistOpt = artistRepo.findBySourceId(artist.getSourceId());

        Artist savedArtist;

        if (existingArtistOpt.isPresent()) {
            // --- UPDATE EXISTING ARTIST ---
            Artist existingArtist = existingArtistOpt.get();

            existingArtist.setName(artist.getName());
            existingArtist.setImageURL(artist.getImageURL());
            // sourceId is the same, so no need to update

            savedArtist = artistRepo.save(existingArtist); // update
        } else {
            // --- CREATE NEW ARTIST ---
            savedArtist = artistRepo.save(artist); // insert
        }

        return ArtistDTO.fromEntity(savedArtist);
    }

    // READ
    
    @Transactional(readOnly = true)
    public List<ArtistDTO> getAllArtists() {
        List<Artist> Artists = artistRepo.findAll();
        return Artists.stream()
                .map(ArtistDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ArtistDTO> getArtistById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Artist ID cannot be null or non-positive for retrieval.");
        }

        Optional<Artist> ArtistFound = artistRepo.findById(id);

        // Map the Optional<Artist> to Optional<ArtistDTO> using map()
        return ArtistFound.map(ArtistDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<ArtistDTO> getArtistBySourceId(String sourceId) { // --- MODIFIED: Return Optional<Artist> ---
        // 1. Input validation for sourceId
        if (sourceId == null || sourceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist Source ID cannot be null or empty for retrieval.");
        }
        Optional<Artist> ArtistFound = artistRepo.findBySourceId(sourceId);

        // Map the Optional<Artist> to Optional<ArtistDTO> using map()
        return ArtistFound.map(ArtistDTO::fromEntity);
    }

    // UPDATE
    @Transactional
    public ArtistDTO updateArtist(Long id, Artist updatedArtistDetails) {
        // 1. Basic input validation for ID and updated details object
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Artist ID cannot be null or non-positive for update.");
        }
        if (updatedArtistDetails == null) {
            throw new IllegalArgumentException("Updated artist details cannot be null.");
        }
    
        // 2. Validate the sourceId from updatedArtistDetails
        if (updatedArtistDetails.getSourceId() == null || updatedArtistDetails.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be empty or null in updated artist details.");
        }
    
        // 3. Retrieve the existing artist using the provided ID
        Artist existingArtist = artistRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist with ID " + id + " not found for update."));
    
        // 4. Check for sourceId uniqueness, excluding the current artist being updated
        if (!existingArtist.getSourceId().equals(updatedArtistDetails.getSourceId())) {
            Optional<Artist> duplicateSourceIdArtist = artistRepo.findBySourceId(updatedArtistDetails.getSourceId());
    
            if (duplicateSourceIdArtist.isPresent()) {
                if (!duplicateSourceIdArtist.get().getId().equals(id)) {
                    throw new RuntimeException("Another artist already exists with source ID '"
                            + updatedArtistDetails.getSourceId() + "'.");
                }
            }
        }
    
        // 5. Apply updates to the existing artist entity
        existingArtist.setName(updatedArtistDetails.getName());
    
        // Only update imageURL if incoming value is NOT "default_image_url"
        if (!"default_image_url".equals(updatedArtistDetails.getImageURL())) {
            existingArtist.setImageURL(updatedArtistDetails.getImageURL());
        }
        existingArtist.setSourceId(updatedArtistDetails.getSourceId()); // Always update sourceId
    
        // 6. Save the updated existing artist (this will perform an UPDATE)
        return ArtistDTO.fromEntity(artistRepo.save(existingArtist));
    }
    

    // DELETE
    @Transactional
    public void deleteArtist(Long id) {
        // 1. Input validation for ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Artist ID cannot be null or non-positive for deletion.");
        }
        // 2. Existence check before deleting
        if (!artistRepo.existsById(id)) {
            throw new EntityNotFoundException("Artist not found with ID: " + id);
        }
        artistRepo.deleteById(id);
    }
    
}