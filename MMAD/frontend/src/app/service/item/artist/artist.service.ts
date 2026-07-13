import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Artist } from '../../../core/model/item/artist.type';

@Injectable({
  providedIn: 'root'
})
export class ArtistService {

  private apiUrl = 'http://localhost:8080/item';
  private searchUrl = 'http://localhost:8080/spotify/search/artist';

  constructor(private http: HttpClient) {}

  // CREATE
  addArtist(artist: Artist): Observable<Artist> {
    return this.http.post<Artist>(`${this.apiUrl}/add`, artist);
  }

  // READ ALL
  getAllArtists(): Observable<Artist[]> {
    return this.http.get<Artist[]>(`${this.apiUrl}/all`);
  }

  // READ BY ID
  getArtistById(id: number): Observable<Artist> {
    return this.http.get<Artist>(`${this.apiUrl}/find/${id}`);
  }

  // READ BY SOURCE ID
  getArtistBySourceId(sourceId: string): Observable<Artist> {
    return this.http.get<Artist>(`${this.apiUrl}/findSource/${sourceId}`);
  }

  // UPDATE
  updateArtist(artist: Artist): Observable<Artist> {
    if (!artist.id) {
      throw new Error('Artist ID is required for update operation.');
    }

    return this.http.put<Artist>(`${this.apiUrl}/update`, artist);
  }

  // DELETE
  deleteArtist(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  // SPOTIFY SEARCH
  searchArtist(name: string): Observable<any> {
    return this.http.get(`${this.searchUrl}/${name}`);
  }
}