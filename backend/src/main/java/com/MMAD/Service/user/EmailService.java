package com.MMAD.Service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService(
            @Value("${resend.api.key}") String apiKey) {

        this.resend = new Resend(apiKey);

    }

    public void sendVerificationEmail(
            String email,
            String code) {

        try {

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("MMAD Music <onboarding@resend.dev>")
                    .to(email)
                    .subject("MMAD Music Verification Code")
                    .html(
                            "<h2>Welcome to MMAD Music!</h2>"
                                    + "<p>Your verification code is:</p>"
                                    + "<h1>" + code + "</h1>"
                                    + "<p>Enter this code to verify your account.</p>")
                    .build();

            resend.emails()
                    .send(params);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send verification email",
                    e);

        }

    }

    public void sendPasswordResetEmail(
            String email,
            String code) {

        try {

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("MMAD Music <onboarding@resend.dev>")
                    .to(email)
                    .subject("MMAD Music Password Reset")
                    .html(
                            "<h2>Password Reset Request</h2>"
                                    + "<p>Your password reset code is:</p>"
                                    + "<h1>" + code + "</h1>"
                                    + "<p>Enter this code to reset your password.</p>")
                    .build();

            resend.emails()
                    .send(params);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send password reset email",
                    e);

        }

    }

}