package com.MMAD.dto.search;

import com.MMAD.model.User.User;

public class UserSearchResultDTO extends SearchResultDTO {


    public UserSearchResultDTO(
            Long id,
            String username,
            String imageURL
    ) {
        super(id, username, imageURL, "user");
    }


    public static UserSearchResultDTO fromEntity(User user) {

        return new UserSearchResultDTO(
                user.getId(),
                user.getUsername(),
                "https://ui-avatars.com/api/?name=" + user.getUsername()
        );
    }
}