import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import { Album } from '../../../model/item/album.type';
import { ItemService } from '../item/item.service';

@Injectable({
  providedIn: 'root'
})
export class AlbumService extends ItemService {
  protected override apiUrl = 'http://localhost:8080/item/albums';
  // External API search URL
  protected searchUrl = 'http://localhost:8080/spotify/search/album';

  constructor(protected override http: HttpClient) {
    super(http);
  }

  // --- Helper for data mapping ---
  private mapToAlbum(data: any): Album {
    return Album.fromJson(data);
  }

  // --- CRUD Operations ---

  // CREATE: Add a new Album
  public addAlbum(album: Album): Observable<Album> {
    return this.http.post<any>(`${this.apiUrl}/add`, album).pipe(
      map(data => this.mapToAlbum(data))
    );
  }

  // READ: Get all Albums
  public getAllAlbums(): Observable<Album[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`).pipe(
      map(response => response.map(data => this.mapToAlbum(data)))
    );
  }

  // READ: Get Album by ID
  public getAlbumById(id: number): Observable<Album> {
    return this.http.get<any>(`${this.apiUrl}/find/${id}`).pipe(
      map(data => this.mapToAlbum(data))
    );
  }

  // READ: Get Album by Source ID
  public getAlbumBySourceId(sourceId: string): Observable<Album> {
    return this.http.get<any>(`${this.apiUrl}/findSource/${sourceId}`).pipe(
      map(data => this.mapToAlbum(data))
    );
  }

  // UPDATE: Update an existing Album
  public updateAlbum(album: Album): Observable<Album> {
    if (album.getId() === null || album.getId() === undefined) {
      throw new Error("Album ID is required for update operation.");
    }
    return this.http.put<any>(`${this.apiUrl}/update`, album).pipe(
      map(data => this.mapToAlbum(data))
    );
  }

  // DELETE: Delete an Album by ID
  public deleteAlbum(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  // --- External API Search ---

  // Search Albums from external API (Spotify)
  public searchAlbums(albumName: string): Observable<Album[]> {
    return this.http.get<any[]>(`${this.searchUrl}/${albumName}`).pipe(
      map(response =>
        response.map((data: any) => this.mapToAlbum(data))
      )
    );
  }
}
