package com.MMAD.MMAD.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.MMAD.model.Review.Review;
import com.MMAD.MMAD.model.Review.ReviewId;

@Repository
public interface ReviewRepo extends JpaRepository<Review, ReviewId> {

    // Find all reviews by username
    List<Review> findByIdUsername(String username);

    // Find all reviews for a specific item type and item id
    List<Review> findByIdItemTypeAndIdItemId(String itemType, Long itemId);

    // Find specific review by username, item id, and item type
    Optional<Review> findByIdUsernameAndIdItemIdAndIdItemType(String username, Long itemId, String itemType);

    // Delete specific review by composite key
    void deleteByIdUsernameAndIdItemIdAndIdItemType(String username, Long itemId, String itemType);
}
