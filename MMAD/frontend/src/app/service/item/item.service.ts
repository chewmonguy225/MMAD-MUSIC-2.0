import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from '../../model/item/item.type';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  protected apiUrl = 'http://localhost:8080/item';

  constructor(protected http: HttpClient) { }

  getItemById(id: number): Observable<Item> {
    return this.http.get<Item>(`${this.apiUrl}/${id}`);
  }

  getAllItems(): Observable<Item[]> {
    return this.http.get<Item[]>(this.apiUrl);
  }
}
