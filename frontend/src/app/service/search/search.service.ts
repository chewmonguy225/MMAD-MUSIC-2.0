import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SearchResponse } from '../../core/dto/search/search-response.model';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private baseUrl = 'http://localhost:8080/search';

  constructor(private http: HttpClient) {}

  search(query: string, types?: string[]): Observable<SearchResponse> {
    const typeParam = types?.length ? `?type=${types.join(',')}` : '';

    return this.http.get<SearchResponse>(
      `${this.baseUrl}/${query}${typeParam}`
    );
  }
}