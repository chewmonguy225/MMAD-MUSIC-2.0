package com.MMAD.MMAD.resource.item;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.MMAD.model.Item.Song.Song;
import com.MMAD.MMAD.model.Item.Song.SongDTO;
import com.MMAD.MMAD.service.item.SongService;

@RestController
@RequestMapping("item/songs")
public class SongResource {

    private final SongService songService;

    public SongResource(SongService songService) {
        this.songService = songService;
    }

    // CREATE
    @PostMapping("/add")
    public ResponseEntity<?> addSong(@RequestBody Song song) {
        try {
            SongDTO newSong = songService.addSong(song);
            return new ResponseEntity<>(newSong, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add song: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ ALL
    @GetMapping("/all")
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        List<SongDTO> songs = songService.getAllSongs();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    // READ BY ID
    @GetMapping("/find/{id}")
    public ResponseEntity<?> getSongById(@PathVariable("id") Long id) {
        try {
            Optional<SongDTO> songDtoOpt = songService.getSongById(id);
            if (songDtoOpt.isPresent()) {
                return new ResponseEntity<>(songDtoOpt.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find song by ID: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ BY SOURCE ID
    @GetMapping("/findSource/{source_id}")
    public ResponseEntity<?> getSongBySourceId(@PathVariable("source_id") String sourceId) {
        try {
            Optional<SongDTO> songDtoOpt = songService.getSongBySourceId(sourceId);
            if (songDtoOpt.isPresent()) {
                return new ResponseEntity<>(songDtoOpt.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find song by Source ID: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSong(@PathVariable("id") Long id, @RequestBody Song songDetails) {
        try {
            SongDTO updatedSongDto = songService.updateSong(id, songDetails);
            return new ResponseEntity<>(updatedSongDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update song: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable("id") Long id) {
        try {
            songService.deleteSong(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete song: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "works";
    }
}
