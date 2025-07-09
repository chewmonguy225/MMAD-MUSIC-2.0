package com.MMAD.MMAD.resource.item;

import java.util.List;

import jakarta.persistence.EntityNotFoundException; // Import EntityNotFoundException
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.MMAD.MMAD.model.Item.Artist;
import com.MMAD.MMAD.service.item.ArtistService; // Ensure this matches the updated service package

@RestController
@RequestMapping("/artist")
public class ArtistResource {
    private final ArtistService artistService;

    public ArtistResource(ArtistService artistService) {
        this.artistService = artistService;
    }

    // CREATE
    @PostMapping("/add")
    public ResponseEntity<?> addArtist(@RequestBody Artist artist) {
        try {
            // Service method renamed to addArtist
            Artist newArtist = artistService.addArtist(artist);
            return new ResponseEntity<>(newArtist, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) { // Catch specific exceptions for better error messages
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // General catch-all for other unexpected errors
            return new ResponseEntity<>("Failed to add artist: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ
    @GetMapping("/all")
    public ResponseEntity<List<Artist>> getAllArtists() {
        List<Artist> artists = artistService.getAllArtists();
        return new ResponseEntity<>(artists, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable("id") Long id) {
        // The service method already throws EntityNotFoundException which Spring will
        // catch
        // and by default return 404. Or you can catch it explicitly.
        Artist artist = artistService.getArtistById(id);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @GetMapping("/findSource/{source_id}")
    public ResponseEntity<Artist> getArtistBySourceId(@PathVariable("source_id") String sourceId) {
        try {
            Artist artist = artistService.getArtistBySourceId(sourceId); // Service now throws exception
            return new ResponseEntity<>(artist, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
        }
    }
    //

    // UPDATE
    @PutMapping("/update/{id}") // Now the ID is part of the URL path
    public ResponseEntity<?> updateArtist(@PathVariable("id") Long id, @RequestBody Artist artist) {
        try {
            // The ID is now coming from the path variable, so no need to check
            // artist.getId() here
            Artist updatedArtist = artistService.updateArtist(id, artist); // Pass both ID and artist
            return new ResponseEntity<>(updatedArtist, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update artist: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}") // Handles DELETE requests to /artist/delete/{id}
    public ResponseEntity<?> deleteArtist(@PathVariable("id") Long id) {
        try {
            artistService.deleteArtist(id);
            // 204 No Content is standard for successful deletions that don't return data
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            // If the artist to delete doesn't exist, return 404 Not Found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Catch-all for unexpected server errors
            return new ResponseEntity<>("Failed to delete artist: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public String getMethodName() {
        String message = "works";
        return message;
    }
}