package com.MMAD.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.dto.review.ItemReviewResponse;
import com.MMAD.model.Review.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    @Query("""
            SELECT new com.MMAD.dto.review.ItemReviewResponse(
                r.id,
                r.rating,
                r.description,
                r.user.username,
                r.createdAt,
                r.updatedAt
            )
            FROM Review r
            WHERE r.item.id = :itemId
            """)
    List<ItemReviewResponse> findReviewResponsesByItemId(@Param("itemId") Long itemId);

    List<Review> findByUserId(Long userId);

    List<Review> findByRatingGreaterThanEqual(int minRating);

    Optional<Review> findByUserIdAndItemId(Long userId, Long itemId);

}
