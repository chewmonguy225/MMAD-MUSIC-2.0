package com.MMAD.MMAD.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MMAD.MMAD.model.Review.Review;


@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> { 
    List<Review> findByItemId(Long itemId);
    List<Review> findByUserId(Long userId);
    List<Review> findByRatingGreaterThanEqual(int minRating);
    Optional<Review> findByUserIdAndItemId(Long userId, Long itemId);
}
