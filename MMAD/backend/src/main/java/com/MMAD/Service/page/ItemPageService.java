package com.MMAD.Service.page;

import java.util.List;

import org.springframework.stereotype.Service;

import com.MMAD.Service.ItemService;
import com.MMAD.Service.ReviewService;
import com.MMAD.Service.SpotifyService;
import com.MMAD.Service.item.AlbumService;
import com.MMAD.dto.item.AlbumDTO;
import com.MMAD.dto.item.ArtistDTO;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.dto.page.ItemPageDTO;
import com.MMAD.dto.review.ItemReviewsResponse;

@Service
public class ItemPageService {

    private final ItemService itemService;
    private final SpotifyService spotifyService;
    private final ReviewService reviewService;

    public ItemPageService(
            ItemService itemService,
            SpotifyService spotifyService,
            ReviewService reviewService) {
        this.itemService = itemService;
        this.spotifyService = spotifyService;
        this.reviewService = reviewService;
    }

    public ItemPageDTO getItemPage(Long itemId) {

        ItemDTO item = itemService.getItemById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        List<ItemPageDTO.SimplifiedSong> songs = null;
        List<AlbumDTO> albums = null;

        if (item instanceof AlbumDTO album) {
            songs = spotifyService.getAlbumTracks(album.getSourceId());
        }

        if (item instanceof ArtistDTO artist) {
            albums = spotifyService.getArtistAlbums(artist.getSourceId());
        }

        ItemReviewsResponse reviewResponse = reviewService.getReviewsByItemId(itemId);

        return new ItemPageDTO(
                item,
                reviewResponse.getReviews(),
                songs,
                albums);
    }
}