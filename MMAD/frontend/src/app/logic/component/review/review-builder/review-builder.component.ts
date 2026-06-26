import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Item } from '../../../model/item/item.type';
import { User } from '../../../model/user.type';
import { RootItemService } from '../../../service/item/root-item.service';
import { Review, ReviewPostRequestPayload } from '../../../model/review.type';
import { ReviewService } from '../../../service/review/review.service';
import { AuthService } from '../../../service/user/auth/auth.service';
import { FormsModule } from '@angular/forms'; // <-- Make sure FormsModule is imported here
import { Observable } from 'rxjs';
import { UserDTO } from '../../../service/user/user.service';

@Component({
  selector: 'app-review-builder',
  imports: [
    CommonModule,
    FormsModule // <-- ADD THIS LINE to the imports array
  ],
  templateUrl: './review-builder.component.html',
  styleUrl: './review-builder.component.css'
})
export class ReviewBuilderComponent {
  @Input() item: Item | null = null;

  @Output() submitted = new EventEmitter<{ description: string; rating: number; item: Item }>();
  @Output() cancelled = new EventEmitter<void>()

  currentUser: UserDTO | null = null;

  description = '';
  rating = 0;
  stars = [1, 2, 3, 4, 5];

  constructor(
    private reviewService: ReviewService,
    private authService: AuthService,
    private rootItemService: RootItemService

  ) { }

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    console.log(this.currentUser?.username)
  }

  setRating(star: number): void {
    this.rating = star;
  }

  onSubmit(): void {

    console.log(this.currentUser);

    // --- Initial Validations (remain the same) ---
    if (!this.description.trim() || this.rating < 1 || this.rating > 5 || !this.item) {
      console.error('Cannot submit review: Please ensure description, rating, and item are valid.');
      // TODO: Provide user feedback for invalid form
      return;
    }

    if (!this.currentUser || !this.currentUser.username) {
      console.error('User not logged in or username is missing. Cannot submit review.');
      // TODO: Show login prompt to user
      return;
    }

    // Subscribe to addItem to ensure it completes before saving the review.
    this.rootItemService.addItem(this.item).subscribe({
      next: (addedItem: Item) => {
        console.log('ReviewBuilder: Item added successfully:', addedItem);
        this.item = addedItem;
        this.saveReviewAfterItemPersisted();
      },
      error: (error) => {
        console.error('ReviewBuilder: Error adding item before review:', error);
        // TODO: Provide user feedback that item saving failed (e.g., show a toast/alert)
      }
    });
  }

  // --- Helper method to encapsulate the review saving logic ---
  // This method will only be called once the item is confirmed to have an ID.
  private saveReviewAfterItemPersisted(): void {
    if (!this.item || !this.item.getId) {
      console.error('Logic error: Item should have an ID before saving review payload.');
      return;
    }

    const reviewPayload: ReviewPostRequestPayload = {
      username: this.currentUser!.username,
      itemId: this.item.getId(),
      rating: this.rating,
      description: this.description.trim()
    };

    console.log('ReviewBuilder: Sending payload to ReviewService:', reviewPayload);

    this.reviewService.saveReview(reviewPayload)
      .subscribe({
        next: (response) => {
          console.log('Review created successfully via ReviewService:', response);
          this.submitted.emit(response); // Emit the new review
          // Reset form fields
          this.description = '';
          this.rating = 0;
          // TODO: Provide user feedback for successful review creation
        },
        error: (error) => {
          console.error('ReviewBuilder: Error creating review via ReviewService:', error);
          // TODO: Provide user feedback about review saving failure (e.g., show a toast/alert)
        }
      });
  }


}


