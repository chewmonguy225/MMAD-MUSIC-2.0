package com.MMAD.MMAD.service;

import com.MMAD.MMAD.model.Review.Review;
import com.MMAD.MMAD.model.Review.ReviewId;
import com.MMAD.MMAD.model.User.User;
import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.model.Item.Artist;
import com.MMAD.MMAD.model.Item.Album;
// import com.MMAD.MMAD.model.Item.Song;

import com.MMAD.MMAD.repo.ArtistRepo;
import com.MMAD.MMAD.repo.AlbumRepo;
// import com.MMAD.MMAD.repo.SongRepo;
import com.MMAD.MMAD.repo.ReviewRepo;
import com.MMAD.MMAD.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final ArtistRepo artistRepo;
    private final AlbumRepo albumRepo;
    // private final SongRepo songRepo;

    @Autowired
    public ReviewService(ReviewRepo reviewRepo,
                         UserRepo userRepo,
                         ArtistRepo artistRepo,
                         AlbumRepo albumRepo) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
        this.artistRepo = artistRepo;
        this.albumRepo = albumRepo;
        // this.songRepo = songRepo;
    }

    public List<Review> findAllReviews() {
        return reviewRepo.findAll();
    }

    public Optional<Review> findByReviewKey(String username, Long itemId, String itemType) {
        return reviewRepo.findByIdUsernameAndIdItemIdAndIdItemType(username, itemId, itemType);
    }

    public List<Review> findByAuthorUsername(String username) {
        return reviewRepo.findByIdUsername(username);
    }

    public List<Review> findByItem(String itemType, Long itemId) {
        return reviewRepo.findByIdItemTypeAndIdItemId(itemType, itemId);
    }

    public Review addReview(Review review) {
        String username = review.getUser().getUsername();
        Long itemId = review.getItem().getId();
        String itemType = review.getItem().getClass().getSimpleName();

        ReviewId id = new ReviewId(username, itemId, itemType);
        review.setId(id);

        if (reviewRepo.existsById(id)) {
            throw new RuntimeException("Review already exists");
        }

        return reviewRepo.save(review);
    }

    public void deleteReview(String username, Long itemId, String itemType) {
        ReviewId id = new ReviewId(username, itemId, itemType);
        if (!reviewRepo.existsById(id)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepo.deleteById(id);
    }

    /**
     * Helper method to find an Item from a specific type and ID.
     */
    public Optional<? extends Item> findItemByTypeAndId(String itemType, Long itemId) {
        return switch (itemType) {
            case "Artist" -> artistRepo.findById(itemId);
            case "Album" -> albumRepo.findById(itemId);
            // case "Song" -> songRepo.findById(itemId);
            default -> Optional.empty();
        };
    }
}
