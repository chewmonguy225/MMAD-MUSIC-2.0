package com.MMAD.dto.review;

import java.util.List;

public class ItemReviewsResponse {

    private Long itemId;
    private List<ItemReviewResponse> reviews;

    public ItemReviewsResponse(Long itemId, List<ItemReviewResponse> reviews) {
        this.itemId = itemId;
        this.reviews = reviews;
    }

    public Long getItemId() {
        return itemId;
    }

    public List<ItemReviewResponse> getReviews() {
        return reviews;
    }
}
