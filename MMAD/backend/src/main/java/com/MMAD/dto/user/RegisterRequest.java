package com.MMAD.dto.user;

public record RegisterRequest(
    String username,
    String email,
    String password
) {}