package com.MMAD.MMAD.model;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
            user.getUsername(),
            user.getFriendsList()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList()),
            user.getPlaylists()     
                .stream()
                .map(Playlist::getId) 
                .collect(Collectors.toList())
        );
    }



}
