import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Review, ReviewPostRequestPayload, UpdateReviewPayload, ReviewGetResponse } from '../../model/review.type';  
import { Artist } from '../../model/item/artist.type';
import { Item } from '../../model/item/item.type';
import { User } from '../../model/user.type';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8080/reviews'; // replace with your real API endpoint

  constructor(private http: HttpClient) {}

  saveReview(reviewPayload: ReviewPostRequestPayload): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, reviewPayload);
  }

  getReviewById(id: number): Observable<Review> { // <--- 1. Specify the return type in the method signature
    if (id <= 0) {
      throw new Error("Review ID must be a positive number.");
    }
    // 2. Specify the expected type to HttpClient.get<Review>()
    return this.http.get<Review>(`${this.apiUrl}/find/${id}`);
  }

  getAllReviews():Observable<Review[]>{
    return this.http.get<Review[]> (`${this.apiUrl}/all`);
  }

  getUserReviews(username: string):Observable<Review[]>{
    return this.http.get<Review[]> (`${this.apiUrl}/user/${username}`);
  }
  
  

  
}
