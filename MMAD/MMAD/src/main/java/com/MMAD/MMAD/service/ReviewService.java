package com.MMAD.MMAD.service;

import com.MMAD.MMAD.model.Review.Review;
import com.MMAD.MMAD.model.Item.Item; // Ensure this path is correct
import com.MMAD.MMAD.model.User.User; // Ensure this path is correct
import com.MMAD.MMAD.model.User.UserDTO;
import com.MMAD.MMAD.repo.ReviewRepo; // Ensure this path is correct

// Assuming you have these services for fetching User and Item entities
import com.MMAD.MMAD.service.item.ItemService; // Ensure this path is correct

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final UserService userService;
    private final ItemService itemService;

    // Constructor injection: Spring will automatically provide instances of
    // ReviewRepo, UserService, and ItemService
    public ReviewService(ReviewRepo reviewRepo, UserService userService, ItemService itemService) {
        this.reviewRepo = reviewRepo;
        this.userService = userService;
        this.itemService = itemService;
    }

    // CREATE

    /**
     * Creates a new review for a given user and item.
     * Throws EntityNotFoundException if user or item does not exist.
     * Throws IllegalArgumentException if the user has already reviewed this item.
     * 
     * @param userId      The ID of the user writing the review.
     * @param itemId      The ID of the item being reviewed.
     * @param rating      The rating (e.g., 1-5).
     * @param description The review text.
     * @return The newly created Review entity.
     */
    @Transactional
    public Review createReview(String username, Long itemId, int rating, String description) {
        // Fetch User and Item entities to establish relationships
        // These methods should throw EntityNotFoundException if the ID is not found.
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        Item item = itemService.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + itemId));

        // Optional: Check if the user has already reviewed this specific item to
        // prevent duplicates
        if (reviewRepo.findByUserIdAndItemId(user.getId(), item.getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "User '" + user.getUsername() + "' has already reviewed item '" + item.getName() + "'.");
        }
        // Create the new Review entity
        Review review = new Review(rating, description, item, user);
        return reviewRepo.save(review); // Save the review to the database
    }

    // READ

    /**
     * Retrieves a review by its ID.
     * 
     * @param id The ID of the review.
     * @return The Review entity.
     * @throws EntityNotFoundException If the review is not found.
     */
    @Transactional(readOnly = true)
    public Review getReviewById(Long id) {
        return reviewRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));
    }

    /**
     * Retrieves all reviews written by a specific user.
     * 
     * @param userId The ID of the user.
     * @return A list of reviews by the user.
     * @throws EntityNotFoundException If the user does not exist.
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByUserId(Long userId) {
        // Fetch User entity first to ensure it exists before querying reviews by it
        UserDTO user = userService.findUserById(userId);
        return reviewRepo.findByUserId(userId);
    }

    /**
     * Retrieves all reviews for a specific item.
     * 
     * @param itemId The ID of the item.
     * @return A list of reviews for the item.
     * @throws EntityNotFoundException If the item does not exist.
     */
    // @Transactional(readOnly = true)
    // public List<Review> getReviewsByItemId(Long itemId) {
    // // Fetch Item entity first to ensure it exists before querying reviews by it
    // Item item = itemService.getItemById(itemId);
    // return reviewRepo.findAllById(item);
    // }

    /**
     * Updates the rating and description of an existing review.
     * 
     * @param reviewId       The ID of the review to update.
     * @param newRating      The new rating.
     * @param newDescription The new description.
     * @return The updated Review entity.
     * @throws EntityNotFoundException If the review is not found.
     */
    @Transactional
    public Review updateReview(Long reviewId, int newRating, String newDescription) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        review.setRating(newRating);
        review.setDescription(newDescription);
        // createdAt and updatedAt are automatically managed by @PrePersist/@PreUpdate
        // in the Review entity

        return reviewRepo.save(review); // Save the updated review
    }

    /**
     * Deletes a review by its ID.
     * 
     * @param reviewId The ID of the review to delete.
     * @throws EntityNotFoundException If the review is not found.
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        // Check if the review exists before attempting to delete it
        if (!reviewRepo.existsById(reviewId)) {
            throw new EntityNotFoundException("Review not found with ID: " + reviewId);
        }
        reviewRepo.deleteById(reviewId); // Delete the review
    }
}