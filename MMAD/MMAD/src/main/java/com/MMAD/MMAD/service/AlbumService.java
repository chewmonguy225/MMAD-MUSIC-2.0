package com.MMAD.MMAD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MMAD.MMAD.model.Item.Album;
import com.MMAD.MMAD.repo.AlbumRepo;

@Service
public class AlbumService {
    private final AlbumRepo albumRepo;
    
    @Autowired
    public AlbumService(AlbumRepo albumRepo) {
        this.albumRepo = albumRepo;
    }

    public Album findAlbumById(Long id) {
        return albumRepo.findById(id).orElseThrow(() -> new RuntimeException("Album not found"));
    }

    public List<Album> findAllAlbums(){
        return albumRepo.findAll();
    }
}
