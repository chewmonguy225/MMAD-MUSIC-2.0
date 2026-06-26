import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import { Artist } from '../../../model/item/artist.type';
import { ItemService } from '../item/item.service'; 

@Injectable({
  providedIn: 'root'
})
export class ArtistService extends ItemService {
  protected override apiUrl = 'http://localhost:8080/item/artists';
  // External API search URL
  protected searchUrl = 'http://localhost:8080/spotify/search/artist';

  constructor(protected override http: HttpClient) {
    super(http);
  }

  // --- Helper for data mapping ---
  // Private method to map raw JSON data into a strongly-typed Artist instance
  // This utilizes the static fromJson method on the Artist model.
  private mapToArtist(data: any): Artist {
    return Artist.fromJson(data);
  }

  // --- CRUD Operations ---

  // CREATE: Add a new Artist
  // Corresponds to backend: POST /artist/add
  public addArtist(artist: Artist): Observable<Artist> {
    // When adding, the 'id' property of the artist object might be null or undefined.
    // The backend will assign it.
    return this.http.post<any>(`${this.apiUrl}/add`, artist).pipe(
      map(data => this.mapToArtist(data))
    );
  }

  // READ: Get all Artists
  // Corresponds to backend: GET /artist/all
  public getAllArtists(): Observable<Artist[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`).pipe(
      map(response => response.map(data => this.mapToArtist(data)))
    );
  }

  // READ: Get Artist by ID
  // Corresponds to backend: GET /artist/find/{id}
  public getArtistById(id: number): Observable<Artist> {
    return this.http.get<any>(`${this.apiUrl}/find/${id}`).pipe(
      map(data => this.mapToArtist(data))
    );
  }

  // READ: Get Artist by Source ID
  // Corresponds to backend: GET /artist/findSource/{source_id}
  // IMPORTANT: Changed sourceID type to 'string' to match backend @PathVariable("source_id") String
  public getArtistBySourceId(sourceId: string): Observable<Artist> {
    return this.http.get<any>(`${this.apiUrl}/findSource/${sourceId}`).pipe(
      map(data => this.mapToArtist(data))
    );
  }

  // UPDATE: Update an existing Artist
  // Corresponds to backend: PUT /artist/update
  // Assumes the `artist` object passed here already contains its `id`.
  public updateArtist(artist: Artist): Observable<Artist> {
    // Best practice to ensure ID is present for an update operation
    if (artist.getId() === null || artist.getId() === undefined) {
      throw new Error("Artist ID is required for update operation.");
    }
    // The backend's @PutMapping("/update") expects the ID within the request body.
    return this.http.put<any>(`${this.apiUrl}/update`, artist).pipe(
      map(data => this.mapToArtist(data))
    );
  }

  // DELETE: Delete an Artist by ID
  // Corresponds to backend: DELETE /artist/delete/{id}
  public deleteArtist(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }


  // --- External API Search ---

  // READ: Search Artists from an external API (e.g., Spotify)
  // Corresponds to backend: GET /spotify/search/artist/{artistName}
  public searchArtists(artistName: string): Observable<Artist[]> {
    return this.http.get<any[]>(`${this.searchUrl}/${artistName}`).pipe(
      map(response =>
        response.map((data: any) => this.mapToArtist(data))
      )
    );
  }
}