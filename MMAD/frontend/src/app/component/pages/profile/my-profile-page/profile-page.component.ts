import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProfileViewerComponent } from '../profile-viewer/profile-viewer.component';

import { ReviewService } from '../../../../service/review/review.service';
import { UserService, UserDTO } from '../../../../service/user/user.service';
import { AuthService } from '../../../../service/user/auth/auth.service';

import { Review } from '../../../../core/model/review/review.type';

import { MatDialog } from '@angular/material/dialog';
import { UserListDialogComponent } from '../../../user-list-dialog/user-list-dialog.component';

import { Router } from '@angular/router';


@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [
    CommonModule,
    ProfileViewerComponent
  ],
  templateUrl: './profile-page.component.html'
})
export class ProfilePageComponent implements OnInit {


  currentUser: UserDTO | null = null;

  reviews: Review[] = [];


  isLoading = true;

  isReviewsLoading = true;


  errorMessage = '';

  cardComponent = 'ReviewCardComponent';



  constructor(
    private userService: UserService,
    private reviewService: ReviewService,
    private authService: AuthService,
    private router: Router,
    private dialog: MatDialog
  ) { }



  ngOnInit(): void {


    this.loadProfile();


  }



  loadProfile() {

    this.isLoading = true;


    this.userService.getMyProfile()
      .subscribe({

        next: (user) => {


          this.currentUser = user;

          this.isLoading = false;


          if (user.username) {

            this.loadReviews(user.username);

          }
          else {

            this.isReviewsLoading = false;

          }

        },


        error: () => {

          this.errorMessage = 'Failed to load profile';

          this.isLoading = false;

          this.isReviewsLoading = false;

        }

      });


  }




  loadReviews(username: string) {


    this.isReviewsLoading = true;


    this.reviewService.getUserReviews(username)
      .subscribe({

        next: (reviews) => {

          this.reviews = reviews ?? [];

          this.isReviewsLoading = false;

        },


        error: () => {

          this.errorMessage = 'Failed to load reviews';

          this.reviews = [];

          this.isReviewsLoading = false;

        }

      });


  }




  logout() {

    this.authService.logout();

    this.router.navigateByUrl('/login');

  }



  openUserList(data: {
    title: string,
    users: string[]
  }) {


    this.dialog.open(UserListDialogComponent, {

      width: '400px',

      data

    });


  }


}