package com.MMAD.dto;

public record MessageResponse(
        boolean success,
        String message
) {}