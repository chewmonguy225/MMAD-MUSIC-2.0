package com.MMAD.Controller.externalAPI;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.Service.SpotifyService;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(
            @PathVariable String query,
            @RequestParam(required = false) String type) {

        List<String> types = (type == null || type.isBlank())
                ? List.of("artist", "album", "track")
                : Arrays.asList(type.toLowerCase().split(","));

        return ResponseEntity.ok(
                spotifyService.searchSpotify(query, types));
    }

    // =========================
    // GET SINGLE ARTIST
    // =========================

    @GetMapping("/artist/{id}")
    public ResponseEntity<?> getArtist(@PathVariable String id) {

        return ResponseEntity.ok(
                spotifyService.getArtist(id));
    }

    // =========================
    // GET SINGLE ALBUM
    // =========================

    @GetMapping("/album/{id}")
    public ResponseEntity<?> getAlbum(@PathVariable String id) {

        return ResponseEntity.ok(
                spotifyService.getAlbum(id));
    }

    // =========================
    // GET SINGLE SONG
    // =========================

    @GetMapping("/song/{id}")
    public ResponseEntity<?> getSong(@PathVariable String id) {

        return ResponseEntity.ok(
                spotifyService.getSong(id));
    }
}