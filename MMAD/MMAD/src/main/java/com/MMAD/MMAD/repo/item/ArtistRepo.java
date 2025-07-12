package com.MMAD.MMAD.repo.item;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.MMAD.model.Item.Artist.Artist;

@Repository
public interface ArtistRepo extends JpaRepository<Artist, Long> {
    void deleteArtistById(Long id);
    Optional<Artist> findById(Long id);
    Optional<Artist> findBySourceId(String sourceId);
    Optional<Artist> findByName(String name);
}