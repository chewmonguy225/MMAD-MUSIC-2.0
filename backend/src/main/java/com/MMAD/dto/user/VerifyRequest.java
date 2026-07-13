package com.MMAD.dto.user;

public record VerifyRequest(
    String email,
    String code
) {}