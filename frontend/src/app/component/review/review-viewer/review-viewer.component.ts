import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReviewCardComponent } from '../global-review-card/review-card.component';
import { ItemReviewCardComponent } from '../item-reviews-card/item-reviews-card.component';
import { ReviewCardSkeletonComponent } from '../global-review-card/review-card-skeleton/review-card-skeleton.component';

@Component({
  selector: 'app-review-viewer',
  standalone: true,
  imports: [
    CommonModule,
    ReviewCardComponent,
    ItemReviewCardComponent,
    ReviewCardSkeletonComponent
  ],
  templateUrl: './review-viewer.component.html',
  styleUrls: ['./review-viewer.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReviewViewerComponent {

  @Input() reviews: any[] | null = null;

  @Input() cardComponent!: String;

  @Input() title: string = 'Reviews';

  @Input() isLoading: boolean = false;

  @Input() errorMessage: string = '';

  skeletonCards = [1, 2, 3];

  trackByReview(index: number, review: any): number {
    return review.id;
  }

}