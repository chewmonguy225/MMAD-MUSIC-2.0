package com.MMAD.MMAD.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MMAD.MMAD.repo.AlbumRepo;

@Service
public class AlbumService {
    private final AlbumRepo albumRepo;
    
    @Autowired
    public AlbumService(AlbumRepo albumRepo) {
        this.albumRepo = albumRepo;
    }   
}
