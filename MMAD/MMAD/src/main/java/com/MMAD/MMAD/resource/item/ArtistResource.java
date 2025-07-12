package com.MMAD.MMAD.resource.item;

import java.util.List;
import java.util.Optional;

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

import com.MMAD.MMAD.model.Item.Artist.Artist;
import com.MMAD.MMAD.model.Item.Artist.ArtistDTO;
import com.MMAD.MMAD.service.item.ArtistService; // Ensure this matches the updated service package

@RestController
@RequestMapping("item/artists")
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
            ArtistDTO newArtist = artistService.addArtist(artist);
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
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        List<ArtistDTO> artists = artistService.getAllArtists();
        return new ResponseEntity<>(artists, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getArtistById(@PathVariable("id") Long id) {
        try {
            Optional<ArtistDTO> artistDtoOpt = artistService.getArtistById(id);
            if (artistDtoOpt.isPresent()) {
                return new ResponseEntity<>(artistDtoOpt.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find artist by ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findSource/{source_id}")
    public ResponseEntity<?> getArtistBySourceId(@PathVariable("source_id") String sourceId) {
        try {
            Optional<ArtistDTO> artistDtoOpt = artistService.getArtistBySourceId(sourceId);
            if (artistDtoOpt.isPresent()) {
                return new ResponseEntity<>(artistDtoOpt.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find Artist by Source ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateArtist(@PathVariable("id") Long id, @RequestBody Artist artistDetails) {
        try {
            ArtistDTO updatedArtistDto = artistService.updateArtist(id, artistDetails);
            return new ResponseEntity<>(updatedArtistDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update Artist: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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