import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import { Song } from '../../../model/item/song.type';
import { ItemService } from '../item/item.service';

@Injectable({
  providedIn: 'root'
})
export class SongService extends ItemService {
  protected override apiUrl = 'http://localhost:8080/item/songs';
  protected searchUrl = 'http://localhost:8080/spotify/search/song';

  constructor(protected override http: HttpClient) {
    super(http);
  }

  // Helper for data mapping
  private mapToSong(data: any): Song {
    return Song.fromJson(data);
  }

  // CREATE: Add a new Song
  public addSong(song: Song): Observable<Song> {
    return this.http.post<any>(`${this.apiUrl}/add`, song).pipe(
      map(data => this.mapToSong(data))
    );
  }

  // READ: Get all Songs
  public getAllSongs(): Observable<Song[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`).pipe(
      map(response => response.map(data => this.mapToSong(data)))
    );
  }

  // READ: Get Song by ID
  public getSongById(id: number): Observable<Song> {
    return this.http.get<any>(`${this.apiUrl}/find/${id}`).pipe(
      map(data => this.mapToSong(data))
    );
  }

  // READ: Get Song by Source ID
  public getSongBySourceId(sourceId: string): Observable<Song> {
    return this.http.get<any>(`${this.apiUrl}/findSource/${sourceId}`).pipe(
      map(data => this.mapToSong(data))
    );
  }

  // UPDATE: Update an existing Song
  public updateSong(song: Song): Observable<Song> {
    if (song.getId() === null || song.getId() === undefined) {
      throw new Error("Song ID is required for update operation.");
    }
    return this.http.put<any>(`${this.apiUrl}/update`, song).pipe(
      map(data => this.mapToSong(data))
    );
  }

  // DELETE: Delete a Song by ID
  public deleteSong(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  // External API Search for Songs
  public searchSongs(songName: string): Observable<Song[]> {
    return this.http.get<any[]>(`${this.searchUrl}/${songName}`).pipe(
      map(response => response.map(data => this.mapToSong(data)))
    );
  }
}
