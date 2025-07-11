package com.MMAD.MMAD.model.Review;

import java.time.LocalDateTime; // Import if you use LocalDateTime in your Review entity
import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.model.User.User;
import com.MMAD.MMAD.model.User.UserDTO;

public class ReviewResponse {
    private Long id;
    private int rating;
    private String description; // Matches backend Review.description

    // Details for the associated Item (assuming you want to show name and ID)
    private Item item;

    // Details for the associated User (assuming you want to show username and ID)
    private UserDTO user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor to easily map from Review entity
    public ReviewResponse(Long id, int rating, String description,
            Item item,
            UserDTO user,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.item = item;
        this.user = user;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    public UserDTO getUser() {
        return user;
    }

    public void setUsername(UserDTO user) {
        this.user = user;
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