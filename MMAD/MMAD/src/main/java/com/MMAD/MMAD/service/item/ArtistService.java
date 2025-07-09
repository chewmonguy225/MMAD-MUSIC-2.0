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

    //CREATE
    @Transactional
    public Artist addArtist(Artist artist) { // <--- Renamed from createArtist to addArtist
        // Add artist-specific validation or business logic here
        return artistRepo.save(artist);
    }

    //READ   
    @Transactional(readOnly = true)
    public List<Artist> getAllArtists() {
        return artistRepo.findAll();
    }
    @Transactional(readOnly = true)
    public Artist getArtistById(Long id) {
        return artistRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public Artist getArtistByName(String name) {
        return artistRepo.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with ID: " + name));
    }

    @Transactional(readOnly = true)
    public Artist getArtistBySourceId(String sourceId) { // <--- Method returns Artist directly, throws exception
        return artistRepo.findBySourceId(sourceId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with Source ID: " + sourceId));
    }

    //UPDATE
    @Transactional
    public Artist updateArtist(Long id, Artist updatedArtistDetails) {
        // First, check if the artist with the given ID exists
        Artist existingArtist = artistRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist with ID " + id + " not found for update."));

        existingArtist.setName(updatedArtistDetails.getName());
        existingArtist.setImageURL(updatedArtistDetails.getImageURL());
        existingArtist.setSourceId(updatedArtistDetails.getSourceId());

        return artistRepo.save(existingArtist);
    }

    //DELETE
    @Transactional
    public void deleteArtist(Long id) {
        if (!artistRepo.existsById(id)) {
            throw new EntityNotFoundException("Artist not found with ID: " + id);
        }
        artistRepo.deleteById(id);
    }
}