import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Review } from '../../model/review.type';  // path to your review model
import { Song } from '../../model/item/song.type';
import { Artist } from '../../model/item/artist.type';
import { Album } from '../../model/item/album.type';
import { Item } from '../../model/item/item.type';
import { User } from '../../model/user.type';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8080/review'; // replace with your real API endpoint

  constructor(private http: HttpClient) {}

  saveReview(review: Review): Observable<any> {
    return this.http.post(this.apiUrl, review);
  }
  

  
}
