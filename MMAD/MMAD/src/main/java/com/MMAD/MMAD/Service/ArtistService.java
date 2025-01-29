package com.MMAD.MMAD.Service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MMAD.MMAD.model.Artist;
import com.MMAD.MMAD.repo.ArtistRepo;

@Service
public class ArtistService {
    private final ArtistRepo artistRepo;
    
    @Autowired
    public ArtistService(ArtistRepo artistRepo){
        this.artistRepo = artistRepo;
    }

    public Artist addArtist (Artist artist){
        return artistRepo.save(artist);
    }

    public void deleteArtist(int id){
        artistRepo.deleteById(id);
    }

    public Artist findArtistById(int id){
        return artistRepo.findArtistById(id).orElseThrow(()-> new ElementNotFoundException("Artist: "+id+" not found"));
    }
}
