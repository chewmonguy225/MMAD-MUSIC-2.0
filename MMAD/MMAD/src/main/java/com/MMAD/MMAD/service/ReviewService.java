package com.MMAD.MMAD.service;

import com.MMAD.MMAD.model.Review.Review;
import com.MMAD.MMAD.model.Review.ReviewResponse;
import com.MMAD.MMAD.model.Item.Item; // Ensure this path is correct
import com.MMAD.MMAD.model.Item.ItemDTO;
import com.MMAD.MMAD.model.User.User; // Ensure this path is correct
import com.MMAD.MMAD.model.User.UserDTO;
import com.MMAD.MMAD.model.User.UserDTOMapper;
import com.MMAD.MMAD.repo.ReviewRepo; // Ensure this path is correct
import org.springframework.data.domain.Sort;

// Assuming you have these services for fetching User and Item entities
import com.MMAD.MMAD.service.item.ItemService; // Ensure this path is correct

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final UserDTOMapper userDTOMapper;
    private final ReviewRepo reviewRepo;
    private final UserService userService;
    private final ItemService itemService;

    // Constructor injection: Spring will automatically provide instances of
    // ReviewRepo, UserService, and ItemService
    public ReviewService(ReviewRepo reviewRepo, UserService userService, ItemService itemService,
            UserDTOMapper userDTOMapper) {
        this.reviewRepo = reviewRepo;
        this.userService = userService;
        this.itemService = itemService;
        this.userDTOMapper = userDTOMapper;
    }

    // CREATE

    /**
     * Creates a new review for a given user and item.
     * Throws IllegalArgumentException if input parameters are invalid or if user
     * has already reviewed this item.
     * Throws EntityNotFoundException if user or item does not exist.
     *
     * @param username    The username of the user writing the review.
     * @param itemId      The ID of the item being reviewed.
     * @param rating      The rating (e.g., 1-5).
     * @param description The review text.
     * @return The newly created Review'd DTO.
     */
    @Transactional
    public ReviewResponse createReview(String username, Long itemId, int rating, String description) {
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

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        ItemDTO item = itemService.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemId));

        if (reviewRepo.findByUserIdAndItemId(user.getId(), item.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "UserAlreadyReviewed");
        }

        Review review = new Review(rating, description, Item.fromDTO(item), user);
        Review savedReview = reviewRepo.save(review);

        // Convert to DTO and return
        return ReviewResponse.fromEntity(savedReview);
    }

    // READ

    /**
     * Retrieves all reviews in the system.
     * 
     * @return A list of all Review DTO in order from most recent to oldest
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviews() {
        return reviewRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(review -> {
                    ItemDTO itemDto = ItemDTO.fromEntity(review.getItem());

                    if (itemDto != null) {
                        System.out.println("Mapping Review ID: " + review.getId() +
                                ", Item type: " + itemDto.getClass().getSimpleName() +
                                " (Name: " + itemDto.getName() + ")");
                    } else {
                        System.out.println("Mapping Review ID: " + review.getId() +
                                ", Item is null or couldn't be mapped to a DTO.");
                    }

                    return new ReviewResponse(
                            review.getId(),
                            review.getRating(),
                            review.getDescription(),
                            itemDto,
                            userDTOMapper.apply(review.getUser()),
                            review.getCreatedAt(),
                            review.getUpdatedAt());
                })
                .toList();
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param id The ID of the review.
     * @return The Review entity.
     * @throws IllegalArgumentException If the review ID is invalid.
     * @throws EntityNotFoundException  If the review is not found.
     */
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Review ID cannot be null or non-positive for retrieval.");
        }

        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));

        return ReviewResponse.fromEntity(review);
    }

    /**
     * Retrieves all reviews written by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of reviews by the user.
     * @throws IllegalArgumentException If the user ID is invalid.
     * @throws EntityNotFoundException  If the user does not exist.
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUsername(String username) {

        // 1. Fetch the User entity (or throw if not found)
        Optional <User> user = userService.getUserByUsername(username);

        // 2. Fetch Reviews by User ID
        List<Review> reviews = reviewRepo.findByUserId(user.get().getId());

        // 3. Convert each Review → ReviewResponse
        return reviews.stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getRating(),
                        review.getDescription(),
                        ItemDTO.fromEntity(review.getItem()), // get and map item
                        userDTOMapper.apply(user.get()), // same user for all reviews
                        review.getCreatedAt(),
                        review.getUpdatedAt()))
                .toList();
    }

    /**
     * Retrieves all reviews for a specific item.
     *
     * @param itemId The ID of the item.
     * @return A list of reviews for the item.
     * @throws IllegalArgumentException If the item ID is invalid.
     * @throws EntityNotFoundException  If the item does not exist.
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByItemId(Long itemId) {
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for retrieving reviews.");
        }

        // 1. Fetch the Item entity (or throw if not found)
        ItemDTO item = itemService.getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemId));

        // 2. Fetch Reviews by Item ID
        List<Review> reviews = reviewRepo.findByItemId(itemId);

        // 3. Convert each Review → ReviewResponse
        return reviews.stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getRating(),
                        review.getDescription(),
                        item,
                        userDTOMapper.apply(review.getUser()),
                        review.getCreatedAt(),
                        review.getUpdatedAt()))
                .toList();
    }

    // UPDATE

    /**
     * Updates the rating and description of an existing review.
     *
     * @param reviewId       The ID of the review to update.
     * @param newRating      The new rating.
     * @param newDescription The new description.
     * @return The updated Review entity.
     * @throws IllegalArgumentException If input parameters are invalid.
     * @throws EntityNotFoundException  If the review is not found.
     */
    @Transactional
    public ReviewResponse updateReview(Long reviewId, int newRating, String newDescription) {
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

        ReviewResponse updatedReview = ReviewResponse.fromEntity(reviewRepo.save(review));
        return updatedReview; // Save the updated review
    }

    // DELETE
    /**
     * Deletes a review by its ID.
     *
     * @param reviewId The ID of the review to delete.
     * @throws IllegalArgumentException If the review ID is invalid.
     * @throws EntityNotFoundException  If the review is not found.
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