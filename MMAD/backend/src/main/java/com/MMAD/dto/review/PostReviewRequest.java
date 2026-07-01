package com.MMAD.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostReviewRequest {

    @NotNull(message = "Item ID is required")
    private Long itemId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    // Constructors
    public PostReviewRequest() {}

    public PostReviewRequest(Long itemId, int rating, String description) {
        this.itemId = itemId;
        this.rating = rating;
        this.description = description;
    }

    // Getters & Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}