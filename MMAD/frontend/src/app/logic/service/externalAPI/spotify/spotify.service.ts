import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Artist } from '../../../model/item/artist.type';
import { Item } from '../../../model/item/item.type';
import { Album } from '../../../model/item/album.type';
import { Song } from '../../../model/item/song.type';
import { ExternalAPIService } from '../external-api.service';

@Injectable({
  providedIn: 'root'
})
export class SpotifyService extends ExternalAPIService {
  protected override apiUrl = 'http://localhost:8080/spotify/';

  constructor(protected override http: HttpClient) { 
    super(http); 
  }

  override searchItem(itemName: string): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.apiUrl}search/item/${itemName}`);
  }

  override searchArtist(artistName: string): Observable<Artist[]> {
    return this.http.get<Artist[]>(`${this.apiUrl}search/artist/${artistName}`);
  }

  override searchAlbum(albumName: string): Observable<Album[]> {
    return this.http.get<Album[]>(`${this.apiUrl}search/album/${albumName}`);
  }

  override searchSong(songName: string): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.apiUrl}search/song/${songName}`);
  }


}
