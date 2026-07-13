import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';

import { UserService } from '../../../../service/user/user.service';
import { AuthPageComponent } from '../auth-page/auth-page.component';


@Component({
  selector: 'app-verify-page',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    AuthPageComponent
  ],
  templateUrl: './verify-page.component.html',
  styleUrls: ['./verify-page.component.css']
})
export class VerifyPageComponent implements OnInit {


  router = inject(Router);
  userService = inject(UserService);


  email: string = '';
  code: string = '';


  errorMessage: string = '';
  successMessage: string = '';
  resendMessage: string = '';


  resendCooldown: number = 0;
  resendDisabled: boolean = false;

  private resendTimer: any;



  ngOnInit() {


    const navigation =
      this.router.getCurrentNavigation();


    this.email =
      navigation?.extras?.state?.['email'] ||
      history.state?.email ||
      '';

  }





  verifyAccount() {


    this.errorMessage = '';
    this.successMessage = '';
    this.resendMessage = '';



    if (!this.email || !this.code) {


      this.errorMessage =
        'Please enter your email and verification code.';


      return;

    }



    this.userService.verifyUser({

      email: this.email,
      code: this.code


    }).subscribe({



      next: () => {


        this.successMessage =
          'Account verified successfully!';


        this.startResendCooldown();

        setTimeout(() => {


          this.router.navigate([
            '/login'
          ]);


        }, 1500);


      },



      error: (err: HttpErrorResponse) => {


        this.errorMessage =
          err.error?.message ||
          err.message ||
          'Invalid verification code.';


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


      this.errorMessage =
        'Email is missing.';


      return;

    }




    this.userService.resendVerification({

      email: this.email


    }).subscribe({



      next: () => {


        this.resendMessage =
          'A new verification code has been sent.';



        this.startResendCooldown();


      },



      error: (err: HttpErrorResponse) => {


        this.errorMessage =
          err.error?.message ||
          err.message ||
          'Could not resend verification code.';


      }


    });


  }






  startResendCooldown() {


    this.resendDisabled = true;

    this.resendCooldown = 60;



    if (this.resendTimer) {

      clearInterval(this.resendTimer);

    }



    this.resendTimer = setInterval(() => {


      this.resendCooldown--;



      if (this.resendCooldown <= 0) {


        clearInterval(this.resendTimer);


        this.resendDisabled = false;

        this.resendCooldown = 0;


      }


    }, 1000);


  }

}