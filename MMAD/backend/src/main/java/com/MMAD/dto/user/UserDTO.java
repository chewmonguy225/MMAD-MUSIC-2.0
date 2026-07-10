package com.MMAD.dto.item;

import java.util.List;

public record UserDTO(
    String username,
    List<String> following,
    List<String> followers
) {}
