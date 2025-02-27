package com.MMAD.MMAD.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.MMAD.model.Album;

@Repository
public interface AlbumRepo extends JpaRepository<Album, Integer> {
    void deleteAlbumById(Long id);

}
