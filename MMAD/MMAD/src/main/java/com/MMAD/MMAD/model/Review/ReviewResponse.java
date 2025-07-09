package com.MMAD.MMAD.model.Review;

import java.time.LocalDateTime; // Import if you use LocalDateTime in your Review entity

public class ReviewResponse {
    private Long id;
    private int rating;
    private String description; // Matches backend Review.description

    // Details for the associated Item (assuming you want to show name and ID)
    private Long itemId;
    private String itemName; // e.g., Item.name

    // Details for the associated User (assuming you want to show username and ID)
    private Long userId;
    private String username; // e.g., User.username

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor to easily map from Review entity
    public ReviewResponse(Long id, int rating, String description,
            Long itemId, String itemName,
            Long userId, String username,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.itemId = itemId;
        this.itemName = itemName;
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getters and Setters ---
    // You can generate these using your IDE (Alt+Insert in IntelliJ, Source ->
    // Generate in Eclipse)
    // Or use Lombok annotations if you have it set up (@Getter, @Setter,
    // @AllArgsConstructor, @NoArgsConstructor)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}