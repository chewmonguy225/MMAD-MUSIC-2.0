package com.MMAD.MMAD.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.service.SpotifyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

import com.MMAD.MMAD.model.Item.Album;
import com.MMAD.MMAD.model.Item.Artist;
import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.model.Item.Song;

@RestController
@RequestMapping("/spotify")
public class SpotifyResource {
    private final SpotifyService spotifyService;

    public SpotifyResource(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search/artist/{artistName}")
    public ResponseEntity<List<Artist>> searchArtist(@PathVariable String artistName) {
        List<Item> items = spotifyService.searchItem(artistName, "artist");

        List<Artist> artists = items.stream()
                .filter(item -> item instanceof Artist) 
                .map(item -> (Artist) item) 
                .collect(Collectors.toList());

        return ResponseEntity.ok(artists);
    }

}
