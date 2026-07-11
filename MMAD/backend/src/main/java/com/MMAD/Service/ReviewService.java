package com.MMAD.Service;

import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.MMAD.Service.item.ItemService;
import com.MMAD.Service.user.UserService;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.dto.review.GetReviewResponse;
import com.MMAD.dto.review.ItemReviewResponse;
import com.MMAD.dto.review.ItemReviewsResponse;
import com.MMAD.dto.user.UserDTOMapper;
import com.MMAD.entity.Review.Review;
import com.MMAD.entity.User.User;
import com.MMAD.entity.item.Item;
import com.MMAD.exception.UserNotFoundException;
import com.MMAD.repo.ReviewRepo;

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
    public GetReviewResponse createReview(Long itemId, int rating, String description) {
        System.out.println("HERE");
        // Input Validation
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive.");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Review description cannot be null or empty.");
        }

        // 🔐 GET USER FROM JWT CONTEXT (NOT FROM PARAMETER)
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        System.out.println("FOUND USER: " + user.getUsername());

        Item item = itemService.getItemEntityById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemId));

        System.out.println("FOUND ITEM: " + item.getName());
        if (reviewRepo.findByUserIdAndItemId(user.getId(), item.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "UserAlreadyReviewed");
        }

        Review review = new Review(rating, description, item, user);
        Review savedReview = reviewRepo.save(review);

        return GetReviewResponse.fromEntity(savedReview);
    }

    // READ

    /**
     * Retrieves all reviews in the system.
     * 
     * @return A list of all Review DTO in order from most recent to oldest
     */
    @Transactional(readOnly = true)
    public List<GetReviewResponse> getAllReviews() {

        return reviewRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(review -> new GetReviewResponse(
                        review.getId(),
                        review.getRating(),
                        review.getDescription(),
                        ItemDTO.fromEntity(review.getItem()),
                        review.getUser().getUsername(),
                        review.getCreatedAt(),
                        review.getUpdatedAt()))
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
    public GetReviewResponse getReviewById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Review ID cannot be null or non-positive for retrieval.");
        }

        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));

        return GetReviewResponse.fromEntity(review);
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
    public List<GetReviewResponse> getReviewsByUsername(String username) {

        // Fetch the User entity (or throw if not found)
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username [%s] not found".formatted(username)));

        // Fetch Reviews by User ID
        List<Review> reviews = reviewRepo.findByUserId(user.getId());

        // Convert each Review → GetReviewResponse
        return reviews.stream()
                .map(review -> new GetReviewResponse(
                        review.getId(),
                        review.getRating(),
                        review.getDescription(),
                        ItemDTO.fromEntity(review.getItem()),
                        user.getUsername(),
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
    public ItemReviewsResponse getReviewsByItemId(Long itemId) {

        // 1. Validate input
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive for retrieving reviews.");
        }

        // 3. Fetch reviews
        List<ItemReviewResponse> reviewResponses = reviewRepo.findReviewResponsesByItemId(itemId);

        return new ItemReviewsResponse(itemId, reviewResponses);
    }

    @Transactional(readOnly = true)
    public List<GetReviewResponse> getFeedReviews(String username) {

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Review> reviews = reviewRepo.findFeedReviews(user.getId());

        return reviews.stream()
                .map(GetReviewResponse::fromEntity)
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
    public GetReviewResponse updateReview(Long reviewId, int newRating, String newDescription) {
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

        GetReviewResponse updatedReview = GetReviewResponse.fromEntity(reviewRepo.save(review));
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