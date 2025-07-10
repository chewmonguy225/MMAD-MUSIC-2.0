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
     * Throws IllegalArgumentException if input parameters are invalid or if user has already reviewed this item.
     * Throws EntityNotFoundException if user or item does not exist.
     *
     * @param username    The username of the user writing the review.
     * @param itemId      The ID of the item being reviewed.
     * @param rating      The rating (e.g., 1-5).
     * @param description The review text.
     * @return The newly created Review entity.
     */
    @Transactional
    public Review createReview(String username, Long itemId, int rating, String description) {
        // Input Validation for parameters
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive.");
        }
        if (rating < 1 || rating > 5) { // Assuming rating is between 1 and 5
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Review description cannot be null or empty.");
        }


        // Fetch User and Item entities to establish relationships
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        // itemService.getItemById already throws EntityNotFoundException and handles ID validation
        Item item = itemService.getItemById(itemId).get();

        // Check if the user has already reviewed this specific item to prevent duplicates
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
     * Retrieves all reviews in the system.
     * @return A list of all Review entities.
     */
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepo.findAll();
    }
      

    /**
     * Retrieves a review by its ID.
     *
     * @param id The ID of the review.
     * @return The Review entity.
     * @throws IllegalArgumentException If the review ID is invalid.
     * @throws EntityNotFoundException If the review is not found.
     */
    @Transactional(readOnly = true)
    public Review getReviewById(Long id) {
        // Input Validation for ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Review ID cannot be null or non-positive for retrieval.");
        }
        return reviewRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));
    }

    // /**
    //  * Retrieves all reviews written by a specific user.
    //  *
    //  * @param userId The ID of the user.
    //  * @return A list of reviews by the user.
    //  * @throws IllegalArgumentException If the user ID is invalid.
    //  * @throws EntityNotFoundException If the user does not exist.
    //  */
    // @Transactional(readOnly = true)
    // public List<Review> getReviewsByUserId(Long userId) {
    //     // Input Validation for User ID
    //     if (userId == null || userId <= 0) {
    //         throw new IllegalArgumentException("User ID cannot be null or non-positive for retrieving reviews.");
    //     }
    //     // Fetch User entity first to ensure it exists before querying reviews by it
    //     // Assuming userService.getUserById returns Optional<User> or throws EntityNotFoundException
    //     User user = userService.getUserById(userId)
    //             .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
    //     return reviewRepo.findByUserId(userId);
    // }

    /**
     * Retrieves all reviews for a specific item.
     *
     * @param itemId The ID of the item.
     * @return A list of reviews for the item.
     * @throws IllegalArgumentException If the item ID is invalid.
     * @throws EntityNotFoundException If the item does not exist.
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByItemId(Long itemId) { // Uncommented and fixed
        // Input Validation for Item ID
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for retrieving reviews.");
        }
        // Fetch Item entity first to ensure it exists before querying reviews by it
        // itemService.getItemById already handles existence and ID validation
        Item item = itemService.getItemById(itemId).get();
        
        // Assuming your ReviewRepo has a method like List<Review> findByItemId(Long itemId);
        return reviewRepo.findByItemId(itemId);
    }


    //UPDATE

    /**
     * Updates the rating and description of an existing review.
     *
     * @param reviewId       The ID of the review to update.
     * @param newRating      The new rating.
     * @param newDescription The new description.
     * @return The updated Review entity.
     * @throws IllegalArgumentException If input parameters are invalid.
     * @throws EntityNotFoundException If the review is not found.
     */
    @Transactional
    public Review updateReview(Long reviewId, int newRating, String newDescription) {
        // Input Validation for parameters
        if (reviewId == null || reviewId <= 0) {
            throw new IllegalArgumentException("Review ID cannot be null or non-positive for update.");
        }
        if (newRating < 1 || newRating > 5) { // Assuming rating is between 1 and 5
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (newDescription == null || newDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Review description cannot be null or empty.");
        }

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        review.setRating(newRating);
        review.setDescription(newDescription);

        return reviewRepo.save(review); // Save the updated review
    }

    //DELETE
       /**
     * Deletes a review by its ID.
     *
     * @param reviewId The ID of the review to delete.
     * @throws IllegalArgumentException If the review ID is invalid.
     * @throws EntityNotFoundException If the review is not found.
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        // Input Validation for ID
        if (reviewId == null || reviewId <= 0) {
            throw new IllegalArgumentException("Review ID cannot be null or non-positive for deletion.");
        }
        // Check if the review exists before attempting to delete it
        if (!reviewRepo.existsById(reviewId)) {
            throw new EntityNotFoundException("Review not found with ID: " + reviewId);
        }
        reviewRepo.deleteById(reviewId); // Delete the review
    }
}