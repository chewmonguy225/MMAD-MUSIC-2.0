package com.MMAD.MMAD.repo.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.MMAD.MMAD.model.Item.Album.Album;

@Repository
public interface AlbumRepo extends JpaRepository<Album, Long> {
    
    void deleteAlbumById(Long id);
    Optional<Album> findBySourceId(String sourceId);
    Optional<Album> findByName(String name);

    @Query("SELECT a FROM Album a LEFT JOIN FETCH a.artists WHERE a.id = :id")
    Optional<Album> findByIdWithArtists(@Param("id") Long id);
    
    @Query("SELECT a FROM Album a LEFT JOIN FETCH a.artists WHERE a.sourceId = :sourceId")
    Optional<Album> findBySourceIdWithArtists(@Param("sourceId") String sourceId);
    
    @Query("SELECT DISTINCT a FROM Album a LEFT JOIN FETCH a.artists")
    List<Album> findAllWithArtists();    
}
