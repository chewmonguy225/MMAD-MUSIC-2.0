import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';

import {
  UserService,
  RegisterRequest,
  LoginRequest
} from '../../../../service/user/user.service';

import { AuthService } from '../../../../service/user/auth/auth.service';
import { LoginResponse } from '../../../../core/dto/login-response.model';
import { isValidEmail, isValidPassword, validatePassword } from '../../../../core/utils/form-validator';

import { AuthPageComponent } from '../auth-page/auth-page.component';
import { PasswordInputComponent } from '../../../password/password-input/password-input/password-input.component';
import { PasswordRulesComponent } from '../../../password/password-rules/password-rules/password-rules.component';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    AuthPageComponent,
    PasswordInputComponent,
    PasswordRulesComponent
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  router = inject(Router);
  userService = inject(UserService);
  authService = inject(AuthService);

  isLoginView: boolean = true;

  showLoginPassword: boolean = false;
  showRegisterPassword: boolean = false;
  showConfirmPassword: boolean = false;

  errorMessage: string = '';
  successMessage: string = '';

  registerObj: RegisterRequest & {
    confirmPassword: string;
  } = {
      username: '',
      email: '',
      password: '',
      confirmPassword: ''
    };

  loginObj: LoginRequest = {
    login: '',
    password: ''
  };

  onRegister() {

    this.errorMessage = '';
    this.successMessage = '';

    if (
      !this.registerObj.username ||
      !this.registerObj.email ||
      !this.registerObj.password ||
      !this.registerObj.confirmPassword
    ) {

      this.errorMessage =
        'Please fill out all required fields.';

      return;

    }

    if (!isValidEmail(this.registerObj.email)) {

      this.errorMessage =
        'Please enter a valid email address.';

      return;

    }

    if (!isValidPassword(this.registerObj.password)) {
      this.errorMessage =
        'Password does not meet the requirements.';
      return;

    }

    if (
      this.registerObj.password !==
      this.registerObj.confirmPassword
    ) {

      this.errorMessage =
        'Passwords do not match.';

      return;

    }
    this.userService.register({

      username: this.registerObj.username,
      email: this.registerObj.email,
      password: this.registerObj.password

    }).subscribe({
      next: () => {
        this.successMessage =
          'Account created! Check your email for the verification code.';
        this.router.navigate(
          ['/verify'],
          {
            state: {
              email: this.registerObj.email
            }
          }
        );
      },
      error: (err) => {
        this.errorMessage =
          err.message ||
          'Registration failed. Please try again.';
      }
    });
  }

  onLogin() {
    this.errorMessage = '';
    this.successMessage = '';

    if (
      !this.loginObj.login ||
      !this.loginObj.password
    ) {
      this.errorMessage = 'Please fill out all required fields.';
      return;
    }

    this.userService.login(this.loginObj)
      .subscribe({
        next: (res: LoginResponse) => {

          this.authService.setSession(
            res.token,
            res.username
          );
        
          this.router.navigateByUrl('home');
        
        },

        error: (err) => {
          this.errorMessage =
            err.message ||
            'Login failed. Please check your credentials and try again.';
        }
      });
  }

  switchView(isLogin: boolean) {
    this.isLoginView = isLogin;
    this.errorMessage = '';
    this.successMessage = '';
  }

  goToForgotPassword() {
    this.router.navigate([
      '/forgot-password'
    ]);
  }

  isValidEmail(email: string): boolean {

    const emailPattern =
      /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    return emailPattern.test(email);

  }

}