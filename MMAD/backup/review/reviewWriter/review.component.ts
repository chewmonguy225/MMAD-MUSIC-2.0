import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Item } from '../../../model/item/item.type';

@Component({
  selector: 'app-review',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent {
  @Input() item: Item | null = null;  // Receive the item here

  @Output() submitted = new EventEmitter<{ item: Item, review: string; rating: number }>();
  @Output() cancel = new EventEmitter<void>();

  reviewText = '';
  rating = 0;
  stars = [1, 2, 3, 4, 5];

  setRating(star: number): void {
    this.rating = star;
  }

  submit(): void {
    if (this.reviewText.trim() && this.item) { // <-- Add check for this.item
      this.submitted.emit({
        review: this.reviewText.trim(),
        rating: this.rating,
        item: this.item // TypeScript now knows this.item is definitely Item here
      });
      this.reviewText = '';
      this.rating = 0;
    } else {
      // Optionally, handle error if item is null, e.g., display a message
      console.error('Cannot submit review: item is missing.');
    }
  }

  cancelReview(): void {
    this.cancel.emit();
  }
}
