package com.MMAD.MMAD.service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Artist> findArtistBySourceId(String sourceId) {
        return artistRepo.findArtistBySourceId(sourceId);
    }

    public Artist addArtist(Artist artist) {
        Optional<Artist> existingArtist = findArtistBySourceId(artist.getSourceId());
        if (existingArtist.isPresent()) {
            return existingArtist.get();
        } else {
            return artistRepo.save(artist);
        }
    }

    public void deleteArtist(int id){
        artistRepo.deleteById(id);
    }

    public List<Artist> findAllArtists(){
        return artistRepo.findAll();
    }
}
