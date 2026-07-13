import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

import { ProfileViewerComponent } from '../profile-viewer/profile-viewer.component';
import { ReviewService } from '../../../../service/review/review.service';
import { UserService, UserDTO } from '../../../../service/user/user.service';
import { Review } from '../../../../core/model/review/review.type';

import { UserListDialogComponent } from '../../../user-list-dialog/user-list-dialog.component';


@Component({
  selector: 'app-user-profile-page',
  standalone: true,
  imports: [
    CommonModule,
    ProfileViewerComponent
  ],
  templateUrl: './user-profile-page.component.html',
  styleUrls: ['./user-profile-page.component.css']
})
export class UserProfilePageComponent implements OnInit {


  user: UserDTO | null = null;

  currentUser: UserDTO | null = null;


  reviews: Review[] = [];


  isLoading = true;

  isReviewsLoading = true;


  errorMessage = '';

  cardComponent = 'ReviewCardComponent';


  isFollowing = false;



  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private reviewService: ReviewService,
    private dialog: MatDialog
  ) { }



  ngOnInit(): void {


    // Load logged in user
    this.userService.getMyProfile()
      .subscribe({

        next: (user) => {

          this.currentUser = user;

          this.updateFollowState();

        }

      });



    // Load profile user
    this.route.params.subscribe(params => {


      const username = params['username'];


      if (username) {

        this.loadUser(username);

      }
      else {

        this.isLoading = false;

      }


    });


  }




  private loadUser(username: string): void {


    this.isLoading = true;


    this.userService.getUserByUsername(username)
      .subscribe({

        next: (res) => {


          this.user = res;


          this.updateFollowState();


          this.loadReviews(username);


        },


        error: () => {


          this.errorMessage = 'Failed to load profile';


          this.isLoading = false;

          this.isReviewsLoading = false;


        }


      });


  }





  loadReviews(username: string): void {


    this.isReviewsLoading = true;


    this.reviewService.getUserReviews(username)
      .subscribe({

        next: (res) => {


          this.reviews = res ?? [];


          this.isReviewsLoading = false;

          this.isLoading = false;


        },


        error: () => {


          this.errorMessage = 'Failed to load reviews';


          this.reviews = [];


          this.isReviewsLoading = false;

          this.isLoading = false;


        }


      });


  }




  refreshReviews(): void {


    if (!this.user) {
      return;
    }


    this.loadReviews(this.user.username);


  }





  private updateFollowState(): void {


    if (!this.currentUser || !this.user) {

      this.isFollowing = false;

      return;

    }


    this.isFollowing =
      this.currentUser.following?.includes(this.user.username) ?? false;


  }





  follow(): void {


    if (!this.currentUser || !this.user) {
      return;
    }



    this.userService.followUser(
      this.currentUser.username,
      this.user.username
    )
      .subscribe({

        next: () => {


          this.isFollowing = true;



          this.currentUser!.following ??= [];

          this.currentUser!.following.push(
            this.user!.username
          );



          this.user!.followers ??= [];

          this.user!.followers.push(
            this.currentUser!.username
          );


        }

      });


  }





  unfollow(): void {


    if (!this.currentUser || !this.user) {
      return;
    }



    this.userService.unfollowUser(
      this.currentUser.username,
      this.user.username
    )
      .subscribe({

        next: () => {


          this.isFollowing = false;



          this.currentUser!.following =
            this.currentUser!.following?.filter(
              u => u !== this.user!.username
            ) ?? [];



          this.user!.followers =
            this.user!.followers?.filter(
              u => u !== this.currentUser!.username
            ) ?? [];


        }

      });


  }





  openUserList(event: {
    title: string,
    users: string[]
  }): void {


    this.dialog.open(UserListDialogComponent, {

      data: {

        title: event.title,

        users: event.users ?? []

      },


      width: '400px'


    });


  }


}