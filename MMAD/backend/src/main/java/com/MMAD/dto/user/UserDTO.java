package com.MMAD.dto.user;

import java.util.List;

public record UserDTO(
    String username,
    String profilePicUrl,
    List<String> following,
    List<String> followers
) {}
