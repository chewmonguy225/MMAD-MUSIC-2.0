package com.MMAD.MMAD.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.service.SpotifyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.model.Item.Album.Album;
import com.MMAD.MMAD.model.Item.Album.AlbumDTO;
//import com.MMAD.MMAD.model.Item.Song;
import com.MMAD.MMAD.model.Item.Artist.Artist;
import com.MMAD.MMAD.model.Item.Artist.ArtistDTO;
import com.MMAD.MMAD.model.Item.Song.Song;
import com.MMAD.MMAD.model.Item.Song.SongDTO;

@RestController
@RequestMapping("/spotify")
public class SpotifyResource {
    private final SpotifyService spotifyService;

    public SpotifyResource(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search/artist/{artistName}")
    public ResponseEntity<List<ArtistDTO>> searchArtist(@PathVariable String artistName) {
        List<Item> items = spotifyService.searchItem(artistName, "artist");

        List<ArtistDTO> artistDTOs = items.stream()
                .filter(item -> item instanceof Artist)
                .map(item -> (Artist) item)
                .map(ArtistDTO::fromEntity) // Convert to DTO
                .collect(Collectors.toList());

        return ResponseEntity.ok(artistDTOs);
    }

    @GetMapping("/search/album/{albumName}")
    public ResponseEntity<List<AlbumDTO>> searchAlbum(@PathVariable String albumName) {
        List<Item> items = spotifyService.searchItem(albumName, "album");

        List<AlbumDTO> albumDTOs = items.stream()
                .filter(item -> item instanceof Album)
                .map(item -> (Album) item)
                .map(AlbumDTO::fromEntity) // Convert Album → AlbumDTO
                .toList();

        return ResponseEntity.ok(albumDTOs);
    }

    @GetMapping("/search/song/{songName}")
    public ResponseEntity<List<SongDTO>> searchSong(@PathVariable String songName) {
        List<Item> items = spotifyService.searchItem(songName, "track");

        List<SongDTO> songDTOs = items.stream()
                .filter(item -> item instanceof Song)
                .map(item -> (Song) item)
                .map(SongDTO::fromEntity) // Convert Song → SongDTO
                .toList();

        return ResponseEntity.ok(songDTOs);
    }

}
