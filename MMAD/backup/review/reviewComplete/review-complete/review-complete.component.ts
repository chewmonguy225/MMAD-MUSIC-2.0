import { Component, OnInit, Input } from '@angular/core';
import { ReviewService } from '../../../../service/review/review.service';
import { Item } from '../../../../model/item/item.type';
import { User } from '../../../../model/user.type';
import { Review } from '../../../../model/review.type';
import { ReviewComponent } from '../../reviewWriter/review.component';
import { AuthService } from '../../../../service/auth/auth.service';

@Component({
  selector: 'app-review-complete',
  templateUrl: './review-complete.component.html',
  styleUrls: ['./review-complete.component.css'],
  imports: [ReviewComponent]
})
export class ReviewCompleteComponent implements OnInit {
  @Input() item: Item | null = null;  // <-- Add this line

  currentUser: User | null = null;
  loading = false;
  message = '';
  showReviewInput = false;

  constructor(
    private reviewService: ReviewService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
  }
  onReviewSubmitted(data: {item: Item, review: string; rating: number }) {
    if (!this.item || !this.currentUser) {
      this.message = 'User or item not loaded.';
      return;
    }

    this.loading = true;
    this.message = '';
 
    // Build Review object to send to backend
    const newReview: Review = {
      user: this.currentUser,
      item: this.item,
      content: data.review,
      rating: data.rating,
      createdAt: new Date()
    };

    this.reviewService.saveReview(newReview).subscribe({
      next: (savedReview) => {
        this.message = 'Review saved successfully!';
        this.loading = false;
        this.showReviewInput = false; // Hide review form after save
      },
      error: (err) => {
        this.message = 'Failed to save review.';
        this.loading = false;
      }
    });
  }

  onReviewCancelled() {
    this.showReviewInput = false; // Hide the review input on cancel
  }
}
