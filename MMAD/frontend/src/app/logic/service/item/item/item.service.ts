import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, forkJoin } from 'rxjs'; // Import throwError, forkJoin
import { Item } from '../../../model/item/item.type';
import { Artist } from '../../../model/item/artist.type';

import { ArtistService } from '../artist/artist.service';


type SpecificItemInstance = Artist //| Song | Album;
type ItemTypeName = 'ARTIST' | 'SONG' | 'ALBUM'; // Still use string literals for retrieval/deletion by type

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  protected apiUrl = 'http://localhost:8080/item';

  constructor(
    protected http: HttpClient,
  ) { }

  //CREATE

  //READ
  getItemById(id: number): Observable<Item> {
    return this.http.get<Item>(`${this.apiUrl}/${id}`);
  }

  getItemBySourceId(sourceId: number): Observable<Item> {
    return this.http.get<Item>(`${this.apiUrl}/findSource/${sourceId}`);
  }

  getAllItems(): Observable<Item[]> {
    return this.http.get<Item[]>(this.apiUrl);
  }

  //UPDATE

  //DELETE
  deleteItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  //SEARCH EXTERNAL
  // Corresponds to backend: GET /spotify/search/artist/{artistName}
  // public searchItem(artistName: string): Observable<Artist[]> {
  //   // return this.http.get<any[]>(`${this.searchUrl}/${artistName}`).pipe(
  //   // //   map(response =>
  //   // //     response.map((data: any) => this.mapToArtist(data))
  //   // //   )
  //   // // );
  // }
}