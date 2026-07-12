package com.MMAD.Controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // For validating @RequestBody
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.MMAD.Service.ReviewService;
import com.MMAD.dto.review.GetReviewResponse;
import com.MMAD.dto.review.ItemReviewsResponse;
import com.MMAD.dto.review.PostReviewRequest;
import com.MMAD.dto.review.UpdateReviewRequest;


import java.util.List;

@RestController
@RequestMapping("/reviews") // Changed to plural '/reviews' for RESTful convention
public class ReviewController {

    private final ReviewService reviewService;

    // Constructor Injection (uncommented)
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;

    }

    // CREATE
    /**
     * Endpoint to create a new review.
     * Requires userId, itemId, rating, and description in the request body.
     */
    @PostMapping("/add")
    public ResponseEntity<?> createReview(
            @Valid @RequestBody PostReviewRequest reviewRequest) {

        try {

            GetReviewResponse savedReview = reviewService.createReview(
                    reviewRequest.getItemId(),
                    reviewRequest.getRating(),
                    reviewRequest.getDescription());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedReview);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();

        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getReason());

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // READ
    @GetMapping("/all")
    public ResponseEntity<List<GetReviewResponse>> getAllReviews() {
        try {
            List<GetReviewResponse> reviews = reviewService.getAllReviews();
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Or include a message body if needed
        }
    }

    @GetMapping("find/{id}") // GET to /reviews/{id}
    public ResponseEntity<GetReviewResponse> getReviewById(@PathVariable("id") Long id) {
        try {
            GetReviewResponse review = reviewService.getReviewById(id);
            return new ResponseEntity<>(review, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * Endpoint to get all reviews by a specific user.
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<GetReviewResponse>> getReviewsByUserId(@PathVariable("username") String username) {
        try {
            List<GetReviewResponse> reviews = reviewService.getReviewsByUsername(username);
            return ResponseEntity.ok(reviews); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500
        }
    }

    /**
     * Endpoint to get all reviews for a specific item.
     */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemReviewsResponse> getReviewsByItemId(
            @PathVariable("itemId") Long itemId) {

        try {
            ItemReviewsResponse response = reviewService.getReviewsByItemId(itemId);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}") // PUT to /reviews/{id}
    public ResponseEntity<GetReviewResponse> updateReview(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateReviewRequest updateRequest) { // <-- CHANGED METHOD SIGNATURE
        try {
            GetReviewResponse updatedReview = reviewService.updateReview(
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

    @GetMapping("/feed")
    public ResponseEntity<List<GetReviewResponse>> getReviewFeed() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        List<GetReviewResponse> reviews = reviewService.getFeedReviews(username);

        return ResponseEntity.ok(reviews);
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