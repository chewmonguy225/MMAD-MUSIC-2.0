import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

import { BasePageComponent } from '../base-page/base-page.component';
import { ReviewViewerComponent } from '../../logic/component/review/review-viewer/review-viewer.component';
import { Review } from '../../logic/model/review.type';
import { ReviewService } from '../../logic/service/review/review.service';
import { AuthService } from '../../logic/service/user/auth/auth.service';
import { UserDTO, UserService } from '../../logic/service/user/user.service';
import { UserListDialogComponent } from '../../logic/component/user-list-dialog/user-list-dialog.component';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [BasePageComponent, CommonModule, ReviewViewerComponent],
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent extends BasePageComponent implements OnInit {

  reviews: Review[] | null = null;
  override user: UserDTO | null = null; // Profile user being viewed
  isFollowing = false;

  constructor(
    private reviewService: ReviewService,
    private userService: UserService,
    private dialog: MatDialog,
    private route: ActivatedRoute,
    authService: AuthService // <-- Make this private for use below
  ) {
    super(authService); // Gives access to logged-in user via `this.loggedInUser`
  }

  override ngOnInit(): void {
    super.ngOnInit?.();

    this.route.params.subscribe(params => {
      const username = params['username'];
      if (username) {
        this.loadUserProfile(username);
      }
    });
  }

  private loadUserProfile(username: string): void {
    this.userService.getUserByUsername(username).subscribe({
      next: user => {
        this.user = user;

        this.updateFollowingStatus(); // Check follow status whenever profile loads

        this.reviewService.getUserReviews(username).subscribe({
          next: plainReviews => {
            this.reviews = plainReviews.map(Review.fromJSON);
          },
          error: err => console.error('Failed to load reviews:', err)
        });
      },
      error: err => console.error('Failed to load user:', err)
    });
  }

  private updateFollowingStatus(): void {
    const loggedInUser = this.authService.getCurrentUser();
    if (loggedInUser && this.user) {
      // Assuming following is an array of UserDTO or objects with username
      this.isFollowing = loggedInUser.following?.some(f => f === this.user!.username) ?? false;
    } else {
      this.isFollowing = false;
    }
  }

  follow(): void {
    const loggedInUser = this.authService.getCurrentUser();
    if (!loggedInUser || !this.user) return;

    this.userService.followUser(loggedInUser.username, this.user.username).subscribe({
      next: () => {
        this.isFollowing = true;
        // Optionally update the logged-in user's following list locally
        loggedInUser.following = loggedInUser.following || [];
        loggedInUser.following.push(this.user!.username);
      },
      error: err => console.error('Failed to follow user:', err)
    });
  }

  unfollow(): void {
    const loggedInUser = this.authService.getCurrentUser();
    if (!loggedInUser || !this.user) return;

    this.userService.unfollowUser(loggedInUser.username, this.user.username).subscribe({
      next: () => {
        this.isFollowing = false;
        if (loggedInUser.following) {
          loggedInUser.following = loggedInUser.following.filter(f => f!== this.user!.username);
        }
      },
      error: err => console.error('Failed to unfollow user:', err)
    });
  }

  openUserList(title: string, users: string[]): void {
    this.dialog.open(UserListDialogComponent, {
      data: { title, users }
    });
  }
  
}
