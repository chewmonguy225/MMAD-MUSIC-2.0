package com.MMAD.MMAD.resource.item;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.MMAD.model.Item.Album.Album;
import com.MMAD.MMAD.model.Item.Album.AlbumDTO;
import com.MMAD.MMAD.service.item.AlbumService;

@RestController
@RequestMapping("item/albums")
public class AlbumResource {
    private final AlbumService albumService;

    public AlbumResource(AlbumService albumService) {
        this.albumService = albumService;
    }

    // CREATE
    @PostMapping("/add")
    public ResponseEntity<?> addAlbum(@RequestBody Album album) {
        try {
            AlbumDTO newAlbum = albumService.addAlbum(album);
            return new ResponseEntity<>(newAlbum, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add album: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ
    @GetMapping("/all")
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        List<AlbumDTO> albums = albumService.getAllAlbums();
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getAlbumById(@PathVariable("id") Long id) {
        try {
            Optional<AlbumDTO> albumDtoOpt = albumService.getAlbumById(id);
            if (albumDtoOpt.isPresent()) {
                return new ResponseEntity<>(albumDtoOpt.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find album by ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findSource/{source_id}")
    public ResponseEntity<?> getAlbumBySourceId(@PathVariable("source_id") String sourceId) {
        try {
            Optional<AlbumDTO> albumDtoOpt = albumService.getAlbumBySourceId(sourceId);
            if (albumDtoOpt.isPresent()) {
                return new ResponseEntity<>(albumDtoOpt.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find album by Source ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAlbum(@PathVariable("id") Long id, @RequestBody Album albumDetails) {
        try {
            AlbumDTO updatedAlbumDto = albumService.updateAlbum(id, albumDetails);
            return new ResponseEntity<>(updatedAlbumDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update album: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable("id") Long id) {
        try {
            albumService.deleteAlbum(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete album: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public String getMethodName() {
        return "works";
    }
}
