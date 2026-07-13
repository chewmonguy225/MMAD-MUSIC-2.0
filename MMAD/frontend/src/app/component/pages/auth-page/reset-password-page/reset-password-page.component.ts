import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { UserService } from '../../../../service/user/user.service';
import { AuthPageComponent } from '../auth-page/auth-page.component';
import { PasswordInputComponent } from '../../../password/password-input/password-input/password-input.component';
import { PasswordRulesComponent } from '../../../password/password-rules/password-rules/password-rules.component';
import { isValidPassword, validatePassword } from '../../../../core/utils/form-validator';
@Component({
  selector: 'app-reset-password-page',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    AuthPageComponent,
    PasswordInputComponent,
    PasswordRulesComponent
  ],
  templateUrl: './reset-password-page.component.html',
  styleUrls: ['./reset-password-page.component.css']
})
export class ResetPasswordPageComponent implements OnInit {

  userService = inject(UserService);
  router = inject(Router);

  email: string = '';
  code: string = '';

  newPassword: string = '';
  confirmPassword: string = '';

  showNewPassword: boolean = false;
  showConfirmPassword: boolean = false;

  errorMessage: string = '';
  successMessage: string = '';

  resendMessage: string = '';

  resendCooldown: number = 0;
  resendDisabled: boolean = false;

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();

    this.email =
      navigation?.extras?.state?.['email'] ||
      history.state?.email ||
      '';
  }

  resetPassword() {
    this.errorMessage = '';
    this.successMessage = '';

    if (
      !this.email ||
      !this.code ||
      !this.newPassword ||
      !this.confirmPassword
    ) {
      this.errorMessage = 'Please fill out all fields.';
      return;
    }

    if (!isValidPassword(this.newPassword)) {

      this.errorMessage =
        'Password does not meet the requirements.';
      return;
    
    }
    
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    this.userService.resetPassword({
      email: this.email,
      code: this.code,
      newPassword: this.newPassword
    }).subscribe({
      next: () => {
        this.successMessage = 'Password reset successfully!';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },

      error: (err: HttpErrorResponse) => {
        this.errorMessage =
          err.error?.message ||
          err.message ||
          'Could not reset password.';
      }
    });
  }

  resendCode() {
    if (this.resendDisabled) {
      return;
    }

    this.errorMessage = '';
    this.resendMessage = '';

    if (!this.email) {
      this.errorMessage = 'Email is missing.';
      return;
    }

    this.userService.forgotPassword({
      email: this.email
    }).subscribe({
      next: () => {
        this.resendMessage = 'A new reset code has been sent.';
        this.startResendCooldown();
      },

      error: (err: HttpErrorResponse) => {
        this.errorMessage =
          err.error?.message ||
          err.message ||
          'Could not resend reset code.';
      }
    });
  }

  startResendCooldown() {
    this.resendDisabled = true;
    this.resendCooldown = 60;

    const timer = setInterval(() => {
      this.resendCooldown--;

      if (this.resendCooldown <= 0) {
        clearInterval(timer);
        this.resendDisabled = false;
        this.resendCooldown = 0;
      }
    }, 1000);
  }

  goToLogin() {
    this.router.navigate([
      '/login'
    ]);
  }
}