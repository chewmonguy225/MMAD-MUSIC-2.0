package com.MMAD.dto.review;

import java.time.LocalDateTime;

public class ItemReviewResponse {

    private Long id;
    private int rating;
    private String description;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItemReviewResponse(Long id, int rating, String description,
                              String username,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public int getRating() { return rating; }
    public String getDescription() { return description; }
    public String getUsername() {return username;}
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}