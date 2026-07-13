package com.MMAD.Controller.item;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.Service.item.SongService;
import com.MMAD.dto.item.SongDTO;

@RestController
@RequestMapping("item/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    // READ ALL
    @GetMapping("/all")
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        List<SongDTO> songs = songService.getAllSongs();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "works";
    }
}
