package com.MMAD.MMAD.repo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.MMAD.model.Item.Album;

@Repository
public interface AlbumRepo extends JpaRepository<Album, Long> {
    void deleteAlbumById(Long id);
    Optional<Album> findAlbumById(Long id);
    Optional<Album> findAlbumBySourceId(String sourceId);
}
