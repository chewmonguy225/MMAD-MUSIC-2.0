package com.MMAD.MMAD.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.model.Artist;
import com.MMAD.MMAD.service.SpotifyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

import com.MMAD.MMAD.model.Artist;
import com.MMAD.MMAD.model.Album;
import com.MMAD.MMAD.model.Song;
import com.MMAD.MMAD.model.Item;

@RestController
@RequestMapping("/spotify")
public class SpotifyResource {
    private final SpotifyService spotifyService;

    public SpotifyResource(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search/artist/{artistName}")
    public ResponseEntity<List<Artist>> searchArtist(@PathVariable String artistName) {
        // Call the service to get the items
        List<Item> items = spotifyService.searchItem(artistName, "artist");

        // Convert the List<Item> to List<Artist> if you're sure that the list only
        // contains Artist objects
        List<Artist> artists = items.stream()
                .filter(item -> item instanceof Artist) // Ensure it's an instance of Artist
                .map(item -> (Artist) item) // Cast it to Artist
                .collect(Collectors.toList());

        return ResponseEntity.ok(artists);
    }

}
