package com.MMAD.MMAD.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MMAD.MMAD.service.AlbumService;

@RestController
@RequestMapping("/album")
public class AlbumResource {
    private final AlbumService albumService;

    public AlbumResource(AlbumService albumService) {
        this.albumService = albumService;
    }

    @RequestMapping("/test")
    public String test() {
        return "Album service is working";
    }
    
}
