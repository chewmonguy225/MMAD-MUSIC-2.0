package com.MMAD.MMAD.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.Service.SpotifyService;

@RestController
@RequestMapping("/spotify")
public class SpotifyResource {

    private final SpotifyService spotifyService;

    public SpotifyResource(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(
            @PathVariable String query,
            @RequestParam String type
    ) {

        switch (type) {

            case "artist":
                return ResponseEntity.ok(spotifyService.searchArtists(query));

            case "album":
                return ResponseEntity.ok(spotifyService.searchAlbums(query));

            case "song":
                return ResponseEntity.ok(spotifyService.searchSongs(query));

            default:
                return ResponseEntity.badRequest()
                        .body("Invalid type: artist | album | song");
        }
    }
}