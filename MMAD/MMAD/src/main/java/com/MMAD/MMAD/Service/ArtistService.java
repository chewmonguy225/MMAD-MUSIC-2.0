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
    public ArtistService(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }

    public Artist findArtistById(int id) {
        return artistRepo.findArtistById(id).orElseThrow(() -> new RuntimeException("Artist not found"));
    }

    /*public Optional<Artist> findArtistBySource_id(String source_id) {
        return artistRepo.findArtistBySource_id(source_id);
    }*/

    public Artist addArtist(Artist artist) {
        //Optional<Artist> existingArtist = findArtistBySource_id(artist.getSource_id());
        //if (existingArtist.isPresent()) {
          //  return existingArtist.get();
        //} else {
            return artistRepo.save(artist);
    //    }
    }

    public void deleteArtist(int id){
        artistRepo.deleteById(id);
    }

    public List<Artist> findAllArtists(){
        return artistRepo.findAll();
    }
}
