package com.MMAD.dto.user;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.MMAD.entity.User.User;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
            user.getUsername(),
            user.getProfilePicUrl(),
            user.getFollowing()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList()),
            user.getFollowers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList())
        );
    }
}
