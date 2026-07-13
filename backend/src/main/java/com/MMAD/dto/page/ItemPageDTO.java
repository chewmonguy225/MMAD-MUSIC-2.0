package com.MMAD.dto.page;

import java.util.List;

import com.MMAD.dto.item.AlbumDTO;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.dto.review.ItemReviewResponse;
import com.MMAD.entity.item.MusicProvider;

public class ItemPageDTO {

    private ItemDTO item;
    private List<ItemReviewResponse> reviews;
    private List<SimplifiedSong> songs;
    private List<AlbumDTO> albums;

    private Integer albumDurationMs;

    // Review summary
    private Double averageRating;
    private Integer reviewCount;

    public ItemPageDTO() {
    }

    public ItemPageDTO(
            ItemDTO item,
            List<ItemReviewResponse> reviews,
            List<SimplifiedSong> songs,
            List<AlbumDTO> albums,
            Integer albumDurationMs,
            Double averageRating,
            Integer reviewCount) {

        this.item = item;
        this.reviews = reviews;
        this.songs = songs;
        this.albums = albums;
        this.albumDurationMs = albumDurationMs;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public List<ItemReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<ItemReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public List<SimplifiedSong> getSongs() {
        return songs;
    }

    public void setSongs(List<SimplifiedSong> songs) {
        this.songs = songs;
    }

    public List<AlbumDTO> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumDTO> albums) {
        this.albums = albums;
    }

    public Integer getAlbumDurationMs() {
        return albumDurationMs;
    }

    public void setAlbumDurationMs(Integer albumDurationMs) {
        this.albumDurationMs = albumDurationMs;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public record SimplifiedSong(
            String name,
            String sourceId,
            MusicProvider provider,
            Integer durationMs,
            List<String> artists) {
    }
}