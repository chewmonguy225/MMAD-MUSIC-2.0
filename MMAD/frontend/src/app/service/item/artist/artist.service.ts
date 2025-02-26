import { Injectable } from '@angular/core';
import { Artist } from '../../../model/item/artist.type';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ItemService } from '../item.service';

@Injectable({
  providedIn: 'root'
})
export class ArtistService extends ItemService {
  protected override apiUrl = 'http://localhost:8080/artist';
  protected searchUrl = 'http://localhost:8080/spotify/search/artist';

  constructor(protected override http: HttpClient) {
    super(http);
  }

  public createArtist(data: any): Artist {
    return new Artist(data.id, data.sourceId, data.name, data.imageURL);
  }

  getArtistWithId(id: number): Observable<Artist> {
    return this.http.get<any>(`${this.apiUrl}/find/${id}`).pipe(
      map(data => this.createArtist(data))
    );
  }

  getArtistWithSourceID(sourceID: number): Observable<Artist> {
    return this.http.get<any>(`${this.apiUrl}/findSource/${sourceID}`).pipe(
      map(data => this.createArtist(data))
    );
  }

  searchArtist(artistName: string): Observable<Artist[]> {
    return this.http.get<any[]>(`${this.searchUrl}/${artistName}`).pipe(
      map(response =>
        response.map((data: any) => this.createArtist(data))
      )
    );
  }
  
}
