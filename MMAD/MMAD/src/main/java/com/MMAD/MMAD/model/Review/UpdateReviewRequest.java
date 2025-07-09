package com.MMAD.MMAD.model.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateReviewRequest {

    @NotNull(message = "Rating is required for update")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating; // Use Integer to allow null if you wanted some fields to be optional

    @NotBlank(message = "Description cannot be empty for update")
    private String description;

    // Constructors
    public UpdateReviewRequest() {}

    public UpdateReviewRequest(Integer rating, String description) {
        this.rating = rating;
        this.description = description;
    }

    // Getters and Setters
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}