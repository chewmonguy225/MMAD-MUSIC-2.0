import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';

import { UserService } from '../../../../service/user/user.service';
import { AuthPageComponent } from '../../auth-page/auth-page/auth-page.component';


@Component({
  selector: 'app-forgot-password-page',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    AuthPageComponent
  ],
  templateUrl: './forgot-password-page.component.html',
  styleUrls: ['./forgot-password-page.component.css']
})
export class ForgotPasswordPageComponent {


  userService = inject(UserService);
  router = inject(Router);


  email: string = '';

  errorMessage: string = '';
  successMessage: string = '';



  sendCode() {

    this.errorMessage = '';
    this.successMessage = '';


    if (!this.email) {

      this.errorMessage =
        'Please enter your email.';

      return;

    }


    this.userService.forgotPassword({
      email: this.email
    })
    .subscribe({

      next: () => {

        this.successMessage =
          'Reset code sent! Check your email.';


        setTimeout(() => {

          this.router.navigate(
            ['/reset-password'],
            {
              state: {
                email: this.email
              }
            }
          );

        }, 1500);

      },


      error: (err) => {

        console.error(
          'Forgot password error:',
          err
        );


        this.errorMessage =
          err.message ||
          'Could not send reset code.';

      }

    });

  }

}