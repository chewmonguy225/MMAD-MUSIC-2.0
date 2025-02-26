import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Artist } from '../../../model/item/artist.type';
import { Item } from '../../../model/item/item.type';
import { ExternalAPIService } from '../external-api.service';

@Injectable({
  providedIn: 'root'
})
export class SpotifyService extends ExternalAPIService {
  protected override apiUrl = 'http://localhost:8080/spotify/';

  constructor(protected override http: HttpClient) { 
    super(http); 
  }

  // The searchArtist method now correctly forms the URL without duplicating the base URL
  override searchItem(itemName: string): Observable<Item[]> {
    return this.http.get<Artist[]>(`${this.apiUrl}search/artist/${itemName}`);
  }
  // The searchArtist method now correctly forms the URL without duplicating the base URL
  override searchArtist(artistName: string): Observable<Artist[]> {
    return this.http.get<Artist[]>(`${this.apiUrl}search/artist/${artistName}`);
  }
}
