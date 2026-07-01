package com.MMAD.dto.user;

public record LoginResponse(
    String token,
    String username
) {}
