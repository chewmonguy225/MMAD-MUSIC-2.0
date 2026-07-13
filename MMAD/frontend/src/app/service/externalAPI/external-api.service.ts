import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Artist } from '../../core/model/item/artist.type';
import { Album } from '../../core/model/item/album.type';
import { Song } from '../../core/model/item/song.type';
import { Item } from '../../core/model/item/item.type';

@Injectable({
  providedIn: 'root'
})
export abstract class ExternalAPIService {

  protected apiUrl = 'http://localhost:8080/';

  constructor(protected http: HttpClient) { }

  // Search
  abstract searchItem(itemName: string): Observable<Item[]>;

  // Lookup by Spotify ID
  abstract getArtist(id: string): Observable<Artist>;

  abstract getAlbum(id: string): Observable<Album>;

  abstract getSong(id: string): Observable<Song>;
}