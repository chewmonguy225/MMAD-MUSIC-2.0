package com.MMAD.MMAD.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.model.Item.Album;
import com.MMAD.MMAD.model.Item.Artist;
import com.MMAD.MMAD.service.AlbumService;

@RestController
@RequestMapping("/album")
public class AlbumResource {
    private final AlbumService albumService;

    public AlbumResource(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Album> getArtistById(@PathVariable("id") Long id){
        Album album = albumService.findAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @RequestMapping("/test")
    public String test() {
        return "Album service is working";
    }
    
}
