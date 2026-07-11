package com.MMAD.dto.user;

public record ResetPasswordRequest(
        String email,
        String code,
        String newPassword
) {}