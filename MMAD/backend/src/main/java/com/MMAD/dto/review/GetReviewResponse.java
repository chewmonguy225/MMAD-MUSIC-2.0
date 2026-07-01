package com.MMAD.dto.review;

import java.time.LocalDateTime;

import com.MMAD.dto.item.ItemDTO;
import com.MMAD.dto.user.UserDTO;
import com.MMAD.dto.user.UserDTOMapper;
import com.MMAD.model.Review.Review;



public class GetReviewResponse {

    private static final UserDTOMapper USER_MAPPER = new UserDTOMapper();

    private Long id;
    private int rating;
    private String description;

    private ItemDTO item;
    private UserDTO user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GetReviewResponse(Long id, int rating, String description,
            ItemDTO item,
            UserDTO user,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.rating = rating;
        this.description = description;
        this.item = item;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // getters/setters unchanged

    // --- Getters and Setters ---
    // You can generate these using your IDE (Alt+Insert in IntelliJ, Source ->
    // Generate in Eclipse)
    // Or use Lombok annotations if you have it set up (@Getter, @Setter,
    // @AllArgsConstructor, @NoArgsConstructor)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static GetReviewResponse fromEntity(Review review) {
        if (review == null)
            return null;

        return new GetReviewResponse(
                review.getId(),
                review.getRating(),
                review.getDescription(),
                ItemDTO.fromEntity(review.getItem()),
                USER_MAPPER.apply(review.getUser()),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }

}