import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ItemPage } from '../../core/model/page/item-page.type';
import { environment } from '../../../environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class PageService {

  protected apiUrl = `${environment.apiUrl}/page`;

  constructor(private http: HttpClient) {}

  getItemPage(itemId: number): Observable<ItemPage> {
    return this.http.get<ItemPage>(
      `${this.apiUrl}/item/${itemId}`
    );
  }
}