import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SearchResponse } from '../../core/dto/search/search-response.model';
import { environment } from '../../../environments/environment.prod'

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  protected apiUrl = `${environment.apiUrl}/search`;

  constructor(private http: HttpClient) {}

  search(query: string, types?: string[]): Observable<SearchResponse> {
    const typeParam = types?.length ? `?type=${types.join(',')}` : '';

    return this.http.get<SearchResponse>(
      `${this.apiUrl}/${query}${typeParam}`
    );
  }
}