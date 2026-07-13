package com.MMAD.Service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.MMAD.Service.item.ItemService;
import com.MMAD.Service.user.UserService;
import com.MMAD.dto.review.GetReviewResponse;
import com.MMAD.dto.review.ItemReviewResponse;
import com.MMAD.dto.review.ItemReviewsResponse;
import com.MMAD.entity.Review.Review;
import com.MMAD.entity.User.User;
import com.MMAD.entity.item.Item;
import com.MMAD.exception.UserNotFoundException;
import com.MMAD.repo.ReviewRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final UserService userService;
    private final ItemService itemService;

    public ReviewService(
            ReviewRepo reviewRepo,
            UserService userService,
            ItemService itemService) {

        this.reviewRepo = reviewRepo;
        this.userService = userService;
        this.itemService = itemService;
    }

    // CREATE

    @Transactional
    public GetReviewResponse createReview(
            Long itemId,
            int rating,
            String description) {

        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("Item ID cannot be null or non-positive.");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Review description cannot be null or empty.");
        }

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Item item = itemService.getItemEntityById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (reviewRepo.findByUserIdAndItemId(user.getId(), item.getId()).isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "UserAlreadyReviewed");
        }

        Review review = new Review(
                rating,
                description,
                item,
                user);

        return GetReviewResponse.fromEntity(
                reviewRepo.save(review));
    }

    // READ

    @Transactional(readOnly = true)
    public List<GetReviewResponse> getAllReviews() {

        List<Review> reviews = reviewRepo.findAll();

        reviews.sort((a, b) -> getReviewSortTime(b)
                .compareTo(getReviewSortTime(a)));

        return reviews.stream()
                .map(GetReviewResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetReviewResponse getReviewById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid review ID");
        }

        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Review not found"));

        return GetReviewResponse.fromEntity(review);
    }

    @Transactional(readOnly = true)
    public List<GetReviewResponse> getReviewsByUsername(String username) {

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found"));

        List<Review> reviews = reviewRepo.findByUserIdOrderByIdDesc(user.getId());

        reviews.sort((a, b) -> getReviewSortTime(b)
                .compareTo(getReviewSortTime(a)));

        return reviews.stream()
                .map(GetReviewResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemReviewsResponse getReviewsByItemId(Long itemId) {

        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid item ID");
        }

        List<ItemReviewResponse> reviews = reviewRepo.findReviewResponsesByItemId(itemId);

        return new ItemReviewsResponse(
                itemId,
                reviews);
    }

    @Transactional(readOnly = true)
    public List<GetReviewResponse> getFeedReviews(String username) {

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found"));

        List<Review> reviews = reviewRepo.findFeedReviews(user.getId());

        reviews.sort((a, b) -> getReviewSortTime(b)
                .compareTo(getReviewSortTime(a)));

        return reviews.stream()
                .map(GetReviewResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<GetReviewResponse> getMyReviewForItem(Long itemId) {

        User user = userService.getCurrentUserEntity();

        return reviewRepo
                .findByUserIdAndItemId(
                        user.getId(),
                        itemId)
                .map(GetReviewResponse::fromEntity);
    }

    // UPDATE

    @Transactional
    public GetReviewResponse updateReview(
            Long reviewId,
            int newRating,
            String newDescription) {

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Review not found"));

        review.setRating(newRating);

        review.setDescription(newDescription);

        reviewRepo.save(review);

        return GetReviewResponse.fromEntity(review);
    }

    // DELETE

    @Transactional
    public void deleteReview(Long reviewId) {

        if (!reviewRepo.existsById(reviewId)) {

            throw new EntityNotFoundException(
                    "Review not found");
        }

        reviewRepo.deleteById(reviewId);
    }

    // SORT HELPER

    private LocalDateTime getReviewSortTime(Review review) {

        return review.getUpdatedAt() != null
                ? review.getUpdatedAt()
                : review.getCreatedAt();
    }

}