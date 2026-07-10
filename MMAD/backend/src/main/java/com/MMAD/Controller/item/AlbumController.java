package com.MMAD.Controller.item;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.Service.item.AlbumService;
import com.MMAD.dto.item.AlbumDTO;
import com.MMAD.model.item.Album;

@RestController
@RequestMapping("item/albums")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // READ
    @GetMapping("/all")
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        List<AlbumDTO> albums = albumService.getAllAlbums();
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }
    
    @GetMapping("/test")
    public String getMethodName() {
        return "works";
    }
}
