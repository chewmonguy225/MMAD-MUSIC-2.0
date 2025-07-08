package com.MMAD.MMAD.model.Review;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReviewId implements Serializable {

    @Column(name = "username")
    private String username;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_type")
    private String itemType;

    public ReviewId() {}

    public ReviewId(String username, Long itemId, String itemType) {
        this.username = username;
        this.itemId = itemId;
        this.itemType = itemType;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    // equals and hashCode for composite key

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewId)) return false;
        ReviewId that = (ReviewId) o;
        return Objects.equals(username, that.username) &&
               Objects.equals(itemId, that.itemId) &&
               Objects.equals(itemType, that.itemType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, itemId, itemType);
    }
}
