package com.MMAD.MMAD.resource;

import com.MMAD.MMAD.model.Review.Review;
import com.MMAD.MMAD.service.ReviewService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review")
public class ReviewResource {

    private final ReviewService reviewService;

    public ReviewResource(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Get all reviews
    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.findAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Get review by composite key (username + itemId + itemType)
    @GetMapping("/find/{username}/{itemId}/{itemType}")
    public ResponseEntity<Review> getReviewById(@PathVariable String username,
                                                @PathVariable Long itemId,
                                                @PathVariable String itemType) {
        Optional<Review> review = reviewService.findByReviewKey(username, itemId, itemType);
        return review.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all reviews by a user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable String username) {
        List<Review> reviews = reviewService.findByAuthorUsername(username);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Get all reviews for an item
    @GetMapping("/item/{itemType}/{itemId}")
    public ResponseEntity<List<Review>> getReviewsByItem(@PathVariable String itemType,
                                                         @PathVariable Long itemId) {
        List<Review> reviews = reviewService.findByItem(itemType, itemId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Add a new review
    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        try {
            Review savedReview = reviewService.addReview(review);
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete a review by composite key
    @DeleteMapping("/delete/{username}/{itemId}/{itemType}")
    public ResponseEntity<?> deleteReview(@PathVariable String username,
                                          @PathVariable Long itemId,
                                          @PathVariable String itemType) {
        try {
            reviewService.deleteReview(username, itemId, itemType);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Simple test
    @GetMapping("/test")
    public String test() {
        return "Review API is working";
    }
}
