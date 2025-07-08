package com.MMAD.MMAD.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.MMAD.model.Item.Artist;

@Repository
public interface ArtistRepo extends JpaRepository<Artist, Long> {
    void deleteArtistById(Long id);
    Optional<Artist> findArtistById(Long id);
    Optional<Artist> findArtistBySourceId(String sourceId);
}