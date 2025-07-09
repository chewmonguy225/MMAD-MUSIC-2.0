package com.MMAD.MMAD.model.Review;

import com.MMAD.MMAD.model.Item.Item; // Import the base Item class
import com.MMAD.MMAD.model.User.User; // Import the User class
import com.MMAD.MMAD.model.User.UserDTO;

import jakarta.persistence.*;
import java.time.LocalDateTime; // Use java.time.LocalDateTime for modern timestamp handling

@Entity
@Table(name = "reviews") // Matches the plural table name in SQL schema
public class Review {

    @Id // Denotes the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated ID, matches BIGINT PRIMARY KEY AUTO_INCREMENT
    private Long id;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false) // Use TEXT for potentially longer descriptions
    private String description;

    @ManyToOne // Many reviews can belong to one Item
    @JoinColumn(name = "item_id", nullable = false) // Foreign key column in 'reviews' table linking to 'items.id'
    private Item item; // The Item entity being reviewed

    @ManyToOne // Many reviews can belong to one User
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column in 'reviews' table linking to 'users.id'
    private User user; // The User entity who wrote the review

    @Column(name = "created_at", updatable = false) // updatable=false as it's set once on creation
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public Review() {
    }

    // Constructor for creating a new review with relationships
    public Review(int rating, String description, Item item, User user) {
        this.rating = rating;
        this.description = description;
        this.item = item;
        this.user = user;
    }

    // Lifecycle callbacks for timestamps
    @PrePersist // Called before the entity is first persisted
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now(); // Initialize updatedAt as well
    }

    @PreUpdate // Called before an entity is updated
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) { // You generally won't call this manually if using @PrePersist
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) { // You generally won't call this manually if using @PreUpdate
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", item=" + (item != null ? item.getName() : "N/A") + // Display item's name
                ", user=" + (user != null ? user.getUsername() : "N/A") + // Assuming User has getUsername()
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}