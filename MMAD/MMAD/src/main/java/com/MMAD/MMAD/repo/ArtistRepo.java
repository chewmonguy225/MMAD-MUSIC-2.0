package com.MMAD.MMAD.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.MMAD.model.Artist;

@Repository
public interface ArtistRepo extends JpaRepository<Artist, Integer> {
    void deleteArtistById(int id);
    Optional<Artist> findArtistById(int id);
    //Optional<Artist> findArtistBySource_id(String source_id);
}