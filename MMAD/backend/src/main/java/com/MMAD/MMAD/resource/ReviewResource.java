package com.MMAD.MMAD.resource;

import com.MMAD.MMAD.service.ReviewService;
import com.MMAD.MMAD.service.item.ItemService;
import com.MMAD.MMAD.service.UserService;
import com.MMAD.MMAD.model.Review.ReviewRequest; // Import the new DTO
import com.MMAD.MMAD.model.Review.ReviewResponse;
import com.MMAD.MMAD.model.Review.UpdateReviewRequest;

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

    // CREATE
    /**
     * Endpoint to create a new review.
     * Requires userId, itemId, rating, and description in the request body.
     */
    @PostMapping("/add")
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        try {
            ReviewResponse savedReview = reviewService.createReview(
                    reviewRequest.getUsername(),
                    reviewRequest.getItemId(),
                    reviewRequest.getRating(),
                    reviewRequest.getDescription());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // or send message
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            System.err.println("Unexpected error creating review: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // READ
    @GetMapping("/all")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        try {
            List<ReviewResponse> reviews = reviewService.getAllReviews();
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Or include a message body if needed
        }
    }

    @GetMapping("find/{id}") // GET to /reviews/{id}
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable("id") Long id) {
        try {
            ReviewResponse review = reviewService.getReviewById(id);
            return new ResponseEntity<>(review, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * Endpoint to get all reviews by a specific user.
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable("username") String username) {
        try {
            List<ReviewResponse> reviews = reviewService.getReviewsByUsername(username);
            return ResponseEntity.ok(reviews); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500
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
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateReviewRequest updateRequest) { // <-- CHANGED METHOD SIGNATURE
        try {
            ReviewResponse updatedReview = reviewService.updateReview(
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