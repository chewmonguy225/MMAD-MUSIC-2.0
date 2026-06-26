package com.MMAD.MMAD.model.User;

import java.util.List;

public record UserDTO(
    String username,
    List<String> following,
    List<String> followers,
    List<Long> playlists
) {}
