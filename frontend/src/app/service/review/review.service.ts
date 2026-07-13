import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReviewPostRequestPayload } from '../../core/model/review/reviewPostRequestPayload.type';
import { ItemReviewsResponse } from '../../core/model/review/ItemReviewsResponse';

import { Review } from '../../core/model/review/review.type';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private apiUrl = 'http://localhost:8080/reviews';

  constructor(private http: HttpClient) { }

  // Create
  createReview(review: ReviewPostRequestPayload): Observable<Review> {
    return this.http.post<Review>(
      `${this.apiUrl}/add`,
      review
    );
  }

  getReviewById(id: number): Observable<Review> {

    if (id <= 0) {
      throw new Error("Review ID must be positive.");
    }

    return this.http.get<Review>(
      `${this.apiUrl}/find/${id}`
    );
  }

  getAllReviews(): Observable<Review[]> {

    return this.http.get<Review[]>(
      `${this.apiUrl}/all`
    );
  }

  getFeedReviews(): Observable<Review[]> {

    return this.http.get<Review[]>(
      `${this.apiUrl}/feed`
    );
  }

  getUserReviews(username: string): Observable<Review[]> {

    return this.http.get<Review[]>(
      `${this.apiUrl}/user/${username}`
    );
  }

  getReviewsByItemId(id: number): Observable<ItemReviewsResponse> {

    return this.http.get<ItemReviewsResponse>(
      `${this.apiUrl}/item/${id}`
    );
  }


  // Get a user's review for a specific item
  getReviewByUserAndItem(
    username: string,
    itemId: number
  ): Observable<Review> {

    return this.http.get<Review>(
      `${this.apiUrl}/me/item/${itemId}`
    );
  }

  updateReview(
    id: number,
    review: {
      rating: number;
      description: string;
    }
  ): Observable<Review> {

    return this.http.put<Review>(
      `${this.apiUrl}/${id}`,
      review
    );
  }

}