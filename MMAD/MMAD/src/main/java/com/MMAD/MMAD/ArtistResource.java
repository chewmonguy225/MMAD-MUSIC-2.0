package com.MMAD.MMAD;

import java.util.List;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.Service.ArtistService;
import com.MMAD.MMAD.model.Artist;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/artist")
public class ArtistResource {
    private final ArtistService artistService;

    public ArtistResource(ArtistService artistService){
        this.artistService = artistService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Artist>> getAllArtists(){
        List<Artist> artists = artistService.findAllArtists();
        return new ResponseEntity<>(artists, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable("id") int id){
        Artist artist = artistService.findArtistById(id);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }
    
    @GetMapping("/findSource/{source_id}")
    public ResponseEntity<Artist> getArtistBySourceId(@PathVariable("source_id") String sourceId){
        Optional<Artist> artist = artistService.findArtistBySourceId(sourceId);
        if(artist.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(artist.get(), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<Artist> addArtist(@RequestBody Artist artist){
        try{
            Artist newArtist = artistService.addArtist(artist);
            return new ResponseEntity<>(newArtist, HttpStatus.CREATED);
        } catch(RuntimeException e) {
            return new ResponseEntity<>(artist, HttpStatus.OK);
        }
        
    }

    @GetMapping("/test")
    public String getMethodName() {
        String message = "works";
        return message;
    }
    
    
    
}
