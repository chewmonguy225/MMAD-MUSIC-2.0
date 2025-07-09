package com.MMAD.MMAD.resource;

import com.MMAD.MMAD.model.Review.Review;
import com.MMAD.MMAD.service.ReviewService;
import com.MMAD.MMAD.model.Review.ReviewRequest; // Import the new DTO
import com.MMAD.MMAD.model.Review.ReviewResponse;
import com.MMAD.MMAD.model.Review.UpdateReviewRequest;

import jakarta.validation.constraints.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // For validating @RequestBody
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException; // Good for handling unique constraint errors

import java.util.List;

@RestController
@RequestMapping("/reviews") // Changed to plural '/reviews' for RESTful convention
public class ReviewResource {

    private final ReviewService reviewService;

    // Constructor Injection (uncommented)
    public ReviewResource(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Endpoint to create a new review.
     * Requires userId, itemId, rating, and description in the request body.
     */
    @PostMapping("/add")
    // --- FIX IS HERE: Change generic type from Review to ReviewResponse ---
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        try {
            // This line is already correct: savedReview is of type ReviewResponse
            Review review = reviewService.createReview(reviewRequest.getUsername(), reviewRequest.getItemId(),
                    reviewRequest.getRating(), reviewRequest.getDescription());

            ReviewResponse savedReview = new ReviewResponse(review.getId(), review.getRating(), review.getDescription()
                , review.getItem().getId(), review.getItem().getName(),
                review.getUser().getId(), review.getUser().getUsername(), 
                review.getCreatedAt(), review.getUpdatedAt());
            // This line is now correct because savedReview is ReviewResponse
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (Exception e) {
            System.err.println("Unexpected error creating review: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    /**
     * Endpoint to get a single review by its primary ID.
     */
    @GetMapping("/{id}") // GET to /reviews/{id}
    public ResponseEntity<Review> getReviewById(@PathVariable("id") Long id) {
        try {
            Review review = reviewService.getReviewById(id);
            return new ResponseEntity<>(review, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * Endpoint to get all reviews by a specific user.
     */
    @GetMapping("/user/{userId}") // GET to /reviews/user/{userId}
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable("userId") Long userId) {
        try {
            List<Review> reviews = reviewService.getReviewsByUserId(userId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // If the user itself is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // /**
    // * Endpoint to get all reviews for a specific item.
    // */
    // @GetMapping("/item/{itemId}") // GET to /reviews/item/{itemId}
    // public ResponseEntity<List<Review>>
    // getReviewsByItemId(@PathVariable("itemId") Long itemId) {
    // try {
    // List<Review> reviews = reviewService.getReviewsByItemId(itemId);
    // return new ResponseEntity<>(reviews, HttpStatus.OK);
    // } catch (EntityNotFoundException e) {
    // // If the item itself is not found
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
    // }
    // }

    // /**
    // * Endpoint to get the average rating for a specific item.
    // */
    // @GetMapping("/item/{itemId}/average-rating") // GET to
    // /reviews/item/{itemId}/average-rating
    // public ResponseEntity<Double>
    // getAverageRatingByItemId(@PathVariable("itemId") Long itemId) {
    // try {
    // // The service method should handle EntityNotFoundException internally
    // // if it retrieves reviews via getReviewsByItemId
    // double averageRating = reviewService.getAverageRatingByItemId(itemId);
    // return new ResponseEntity<>(averageRating, HttpStatus.OK);
    // } catch (EntityNotFoundException e) {
    // // If the item itself is not found during review retrieval
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
    // }
    // }

    /**
     * Endpoint to update an existing review.
     * Note: This assumes you only allow updating rating and description.
     * If user/item can be changed, you'd need a different DTO.
     */
    @PutMapping("/{id}") // PUT to /reviews/{id}
    public ResponseEntity<Review> updateReview(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateReviewRequest updateRequest) { // <-- CHANGED METHOD SIGNATURE
        try {
            Review updatedReview = reviewService.updateReview(
                    id,
                    updateRequest.getRating(),
                    updateRequest.getDescription());
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        } catch (Exception e) {
            // It's good practice to log the actual exception for debugging
            System.err.println("Error updating review: " + e.getMessage());
            e.printStackTrace(); // For full stack trace in console
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for other issues (e.g., validation)
        }
    }

    /**
     * Endpoint to delete a review by its ID.
     */
    @DeleteMapping("/{id}") // DELETE to /reviews/{id}
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long id) {
        try {
            reviewService.deleteReview(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * Simple test endpoint to check if the API is responsive.
     */
    @GetMapping("/test") // GET to /reviews/test
    public String test() {
        return "Review API is working";
    }
}