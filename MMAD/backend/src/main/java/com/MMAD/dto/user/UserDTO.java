package com.MMAD.dto.user;

import java.util.List;

public record UserDTO(
    String username,
    List<String> following,
    List<String> followers,
    List<Long> playlists
) {}
