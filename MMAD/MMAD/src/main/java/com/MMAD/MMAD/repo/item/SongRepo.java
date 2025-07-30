package com.MMAD.MMAD.repo.item;

import com.MMAD.MMAD.model.Item.Song.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepo extends JpaRepository<Song, Long> {

    Optional<Song> findBySourceId(String sourceId);

    Optional<Song> findByName(String name);

    @Query("SELECT s FROM Song s LEFT JOIN FETCH s.album LEFT JOIN FETCH s.artists WHERE s.id = :id")
    Optional<Song> findByIdWithAlbumAndArtist(@Param("id") Long id);    

    @Query("SELECT s FROM Song s LEFT JOIN FETCH s.album LEFT JOIN FETCH s.artists WHERE s.sourceId = :sourceId")
    Optional<Song> findBySourceIdWithAlbumAndArtists(@Param("sourceId") String sourceId);    

    @Query("SELECT DISTINCT s FROM Song s LEFT JOIN FETCH s.album LEFT JOIN FETCH s.artists")
    List<Song> findAllWithAlbumAndArtist();
}
