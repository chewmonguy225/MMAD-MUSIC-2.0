// File: com/MMAD/MMAD/service/item/ArtistService.java
package com.MMAD.MMAD.service.item; // <--- IMPORTANT: Changed package here

import com.MMAD.MMAD.model.Item.Artist;
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
    public Artist addArtist(Artist artist) {
        //1: Validate that sourceId is provided (if it's mandatory for uniqueness)
        if (artist.getSourceId() == null || artist.getSourceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source ID cannot be empty or null for a new artist.");
        }

        //2: Check if an artist with the given sourceId already exists
        Optional<Artist> existingArtist = artistRepo.findBySourceId(artist.getSourceId());

        if (existingArtist.isPresent()) {
            return existingArtist.get();
        } else {
            // If no existing artist found by sourceId, proceed with saving the new artist
            // This will perform an INSERT (assuming 'artist.id' is null for new entities).
            return artistRepo.save(artist);
        }
    }

    //READ   
    @Transactional(readOnly = true)
    public List<Artist> getAllArtists() {
        return artistRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Artist> getArtistById(Long id) { // Changed return type to Optional
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Artist ID cannot be null or non-positive for retrieval.");
        }
        return artistRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Artist> getArtistByName(String name) { // Changed return type to Optional
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be null or empty for retrieval.");
        }
        return artistRepo.findByName(name);
    }

    @Transactional(readOnly = true)
    public Optional<Artist> getArtistBySourceId(String sourceId) { // Changed return type to Optional
        if (sourceId == null || sourceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist Source ID cannot be null or empty for retrieval.");
        }
        return artistRepo.findBySourceId(sourceId);
    }


    //UPDATE
    @Transactional
    public Artist updateArtist(Long id, Artist updatedArtistDetails) {
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
        // This check is only necessary if the sourceId is actually being changed
        if (!existingArtist.getSourceId().equals(updatedArtistDetails.getSourceId())) {
            Optional<Artist> duplicateSourceIdArtist = artistRepo.findBySourceId(updatedArtistDetails.getSourceId());

            if (duplicateSourceIdArtist.isPresent()) {
                // If a duplicate is found, ensure it's NOT the artist we are currently updating.
                // If the found artist has a different ID, then it's a true conflict.
                if (!duplicateSourceIdArtist.get().getId().equals(id)) {
                    throw new RuntimeException("Another artist already exists with source ID '" + updatedArtistDetails.getSourceId() + "'.");
                }
            }
        }

        // 5. Apply updates to the existing artist entity
        existingArtist.setName(updatedArtistDetails.getName());
        existingArtist.setImageURL(updatedArtistDetails.getImageURL());
        existingArtist.setSourceId(updatedArtistDetails.getSourceId()); // Update the sourceId with the new value

        // 6. Save the updated existing artist (this will perform an UPDATE)
        return artistRepo.save(existingArtist);
    }

    //DELETE
    @Transactional
    public void deleteArtist(Long id) {
        // 1. Input validation for ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for deletion.");
        }
        // 2. Existence check before deleting
        if (!artistRepo.existsById(id)) {
            throw new EntityNotFoundException("Item not found with ID: " + id);
        }
        artistRepo.deleteById(id);
    }
}