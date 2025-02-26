import { Injectable, inject } from '@angular/core';
import { Artist } from '../../model/artist.type'
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ArtistService {
  private apiUrl = 'http://localhost:8080/artist/find/';

  constructor(private http: HttpClient) { }

  getArtistWithId(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}${id}`);
  }
}
