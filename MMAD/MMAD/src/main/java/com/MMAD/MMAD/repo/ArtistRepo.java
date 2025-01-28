package com.MMAD.MMAD.repo;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import com.MMAD.MMAD.model.Artist;

public interface ArtistRepo extends JpaRepository<Artist, Integer> {
    void deleteArtistById(int id);
    Optional<Artist> findArtistById(int id);
    
}
