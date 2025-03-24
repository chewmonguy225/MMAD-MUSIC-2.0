package com.MMAD.MMAD.model;

import java.util.List;

public record UserDTO(
    String username,
    List<String> friends,
    List<Long> playlists
) {}
