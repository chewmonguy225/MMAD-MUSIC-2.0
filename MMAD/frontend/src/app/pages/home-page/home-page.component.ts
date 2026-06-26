import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router'; // Assuming you use router-outlet
import { ReviewViewerComponent } from '../../logic/component/review/review-viewer/review-viewer.component';
import { BasePageComponent } from '../base-page/base-page.component';
import { AuthService } from '../../logic/service/user/auth/auth.service';
import { ReviewService } from '../../logic/service/review/review.service';
import { Review } from '../../logic/model/review.type';

@Component({
  selector: 'app-home', // Or whatever your home component's selector is
  standalone: true, // Assuming your home component is also standalone
  imports: [
    BasePageComponent,
    ReviewViewerComponent
  ],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent extends BasePageComponent implements OnInit{
  reviews: Review[] | null = null;

  constructor(private reviewService: ReviewService, authService: AuthService) {
    super(authService);
  }
  override ngOnInit(): void {
    super.ngOnInit?.();
    this.reviewService.getAllReviews().subscribe({
      next: (plainReviews: any[]) => {
        // Convert plain JSON to Review instances
        this.reviews = plainReviews.map(reviewJson => Review.fromJSON(reviewJson));
        console.log('User reviews received:', this.reviews); // ✅
      },
      error: (err) => console.error('Failed to load user reviews:', err)
    });
  }

}