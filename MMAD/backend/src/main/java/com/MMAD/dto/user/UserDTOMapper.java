package com.MMAD.dto.user;
import com.MMAD.dto.item.UserDTO;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.MMAD.model.User.User;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
            user.getUsername(),
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
