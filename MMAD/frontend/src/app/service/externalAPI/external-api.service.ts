import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Artist } from '../../model/item/artist.type';
import { Item } from '../../model/item/item.type';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export abstract class ExternalAPIService {
  protected apiUrl = 'http://localhost:8080/';
  constructor(protected http: HttpClient) { }

  abstract searchItem(itemName: String): Observable<Item[]>;

  abstract searchArtist(artistName: String): Observable<Artist[]>;

}
