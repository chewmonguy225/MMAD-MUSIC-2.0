package com.MMAD.Service.item;

import org.springframework.stereotype.Service;

import com.MMAD.Service.SpotifyService;
import com.MMAD.dto.item.ArtistDTO;
import com.MMAD.model.item.Artist;
import com.MMAD.repo.item.ItemRepo;

import jakarta.transaction.Transactional;

@Service
public class ItemUpdateService {

    private final SpotifyService spotifyService;
    private final ItemRepo itemRepo;

    public ItemUpdateService(
            SpotifyService spotifyService,
            ItemRepo itemRepo
    ){
        this.spotifyService = spotifyService;
        this.itemRepo = itemRepo;
    }


    @Transactional
    public Artist enrichArtist(Artist artist){

        if(
            artist.getImageURL() != null &&
            !artist.getImageURL().equals("default")
        ){
            return artist;
        }


        ArtistDTO dto =
            spotifyService.getArtist(
                artist.getSourceId()
            );


        artist.setImageURL(
            dto.getImageURL()
        );


        return itemRepo.save(artist);
    }
}
