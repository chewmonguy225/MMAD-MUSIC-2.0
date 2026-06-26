import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService, UserDTO } from '../../service/user/user.service';
import { NgIf } from '@angular/common';
import { AuthService } from '../../service/user/auth/auth.service';
import { LoginResponse } from '../../service/user/auth/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  router = inject(Router);
  userService = inject(UserService);
  authService = inject(AuthService);

  isLoginView: boolean = false;
  errorMessage: string = '';

  registerObj: any = {
    username: '',
    password: ''
  }

  loginObj: any = {
    username: '',
    password: ''
  }


  onRegister() {
    this.errorMessage = '';
    if(!this.registerObj.username || !this.registerObj.password) {
      this.errorMessage = 'Please fill out all required fields.';
      return;
    }
    this.userService.register(this.registerObj).subscribe({
      next: (res:any) => {
          alert("Registration success");
          this.isLoginView = true;
          this.loginObj.username = this.registerObj.username;
          this.loginObj.password = '';
      },
      error: (err) => {
        console.error("Registration error:", err);
        this.errorMessage = err.error?.message || 'Username already taken. Please choose a different one.';
      }
    });
  }


  onLogin() {
    this.errorMessage = '';
    if (!this.loginObj.username || !this.loginObj.password) {
      this.errorMessage = 'Please fill out all required fields.';
      return;
    }

    this.userService.login(this.loginObj).subscribe({
      // >>>>>> THE KEY CHANGE IS HERE <<<<<<
      // Expect the response directly as a UserDTO (or 'any' if not strictly typed in UserService)
      // If your backend doesn't send a JWT token back on login, you don't have one to store.
      next: (loggedInUser: UserDTO) => { // <-- Changed 'res' to 'loggedInUser' and its type
        console.log('LoginComponent: Login successful response (actual):', loggedInUser);
        console.log('LoginComponent: User received from backend (actual):', loggedInUser); // This will now show the user object
        console.log(this.authService.getCurrentUser()?.username);
        alert("Login success");

        // Assuming your backend doesn't send a 'token' in this response,
        // you'll pass a dummy string for the token, or adjust AuthService.setSession
        // If your backend IS supposed to send a token, you need to fix the backend.
        const dummyToken = 'no-token-from-backend'; // Or retrieve from headers if sent there
        this.authService.setSession(dummyToken, loggedInUser); // Pass the directly received user

        this.router.navigateByUrl('home');
      },
      error: (err) => {
        console.error("Login error:", err);
        this.errorMessage = err.error?.message || 'Login failed. Please check your credentials and try again.';
      }
    });
  }


  switchView(isLogin: boolean) {
    this.isLoginView = isLogin;
    this.errorMessage = '';
  }

}