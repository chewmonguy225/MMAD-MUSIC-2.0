package com.MMAD.MMAD.model.Review;

import com.MMAD.MMAD.model.User.User;
import com.MMAD.MMAD.model.Item.Item;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "review")
public class Review {

    @EmbeddedId
    private ReviewId id;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username")
    private User user;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Transient
    private Item item; // not stored in DB, used in logic layer

    public Review() {}

    public Review(User user, Item item, int rating, String text) {
        this.user = user;
        this.item = item;
        this.rating = rating;
        this.text = text;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.id = new ReviewId(user.getUsername(), item.getId(), item.getClass().getSimpleName());
    }

    // Getters and setters

    public ReviewId getId() {
        return id;
    }

    public void setId(ReviewId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (id != null) {
            id.setUsername(user.getUsername());
        }
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        if (item != null && id != null) {
            id.setItemId(item.getId());
            id.setItemType(item.getClass().getSimpleName());
        }
    }
}
